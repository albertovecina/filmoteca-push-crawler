package com.avs.filmoteca

import com.avs.filmoteca.data.domain.Region
import com.avs.filmoteca.data.domain.UpdateStatus
import com.avs.filmoteca.data.repository.DataRepository
import rx.Observable
import rx.Observer

class App(private val region: Region = Region.Albacete) : Observer<List<String>> {

    private val repository = DataRepository.instance
    private var currentMovies: List<String> = ArrayList()

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val app = if (args.isNotEmpty())
                App(Region.fromCode(args[0]))
            else
                App()
            app.init()
        }

    }

    fun init() {
        println("Init")
        println("Region: $region")
        Observable
            .zip<List<String>, List<String>, List<String>>(
                repository.getStoredMoviesObservable(region),
                repository.getPublishedMoviesObservable(region).map { it.map { movie -> movie.title } }
            ) { oldMovies, newMovies ->
                currentMovies = newMovies
                substractNewMovies(oldMovies, newMovies)
            }
            .toBlocking()
            .subscribe(this)
    }

    private fun substractNewMovies(oldMovies: List<String>?, newMovies: List<String>?): List<String> =
        if (oldMovies != null && newMovies != null)
            newMovies.filter { !oldMovies.contains(it) }
        else
            ArrayList()

    override fun onCompleted() {
        println("Completed")
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(addedMovies: List<String>) {
        addedMovies.forEach { println(it) }
        val isUpdating = addedMovies.isNotEmpty()
        if (isUpdating)
            updateMovies(currentMovies)
        if (needToSendPush(isUpdating))
            sendPushNotification()
    }

    private fun sendPushNotification() {
        repository.getRegistrationIdsObservable(region)
            .flatMap { registrationIds ->
                repository.getPushDeliveryObservable(registrationIds)
            }
            .toBlocking()
            .subscribe()
    }

    private fun updateMovies(movies: List<String>) {
        repository.getUpdateMoviesObservable(region, movies)
            .toBlocking()
            .subscribe()
    }

    private fun needToSendPush(isUpdating: Boolean): Boolean {
        var needToSendPush = false
        if (isUpdating) {
            repository.setUpdateStatus(region, UpdateStatus.UPDATING)
        } else {
            when (repository.getLastUpdateStatus(region)) {
                UpdateStatus.UPDATING -> repository.setUpdateStatus(region, UpdateStatus.UPDATE_IDLE_1)
                UpdateStatus.UPDATE_IDLE_1 -> repository.setUpdateStatus(region, UpdateStatus.UPDATE_IDLE_2)
                UpdateStatus.UPDATE_IDLE_2 -> {
                    repository.setUpdateStatus(region, UpdateStatus.NOT_UPDATING)
                    needToSendPush = true
                }
            }
        }
        return needToSendPush
    }

}
