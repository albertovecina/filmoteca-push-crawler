package com.avs.filmoteca

import com.avs.filmoteca.data.domain.UpdateStatus
import com.avs.filmoteca.data.repository.DataRepository
import rx.Observable
import rx.Observer

class App : Observer<List<String>> {

    private val repository = DataRepository.instance
    private var currentMovies: List<String> = ArrayList()

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val app = App()
            app.init()
        }

    }

    fun init() {
        println("Init")
        Observable
                .zip<List<String>, List<String>, List<String>>(
                        repository.getStoredMoviesObservable(),
                        repository.getPublishedMoviesObservable().map { it.map { movie -> movie.title } }
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
        repository.getRegistrationIdsObservable()
                .flatMap { registrationIds ->
                    repository.getPushDeliveryObservable(registrationIds)
                }
                .toBlocking()
                .subscribe()
    }

    private fun updateMovies(movies: List<String>) {
        repository.getUpdateMoviesObservable(movies)
                .toBlocking()
                .subscribe()
    }

    private fun needToSendPush(isUpdating: Boolean): Boolean {
        var needToSendPush = false
        if (isUpdating) {
            repository.setUpdateStatus(UpdateStatus.UPDATING)
        } else {
            when (repository.getLastUpdateStatus()) {
                UpdateStatus.UPDATING -> repository.setUpdateStatus(UpdateStatus.UPDATE_IDLE_1)
                UpdateStatus.UPDATE_IDLE_1 -> repository.setUpdateStatus(UpdateStatus.UPDATE_IDLE_2)
                UpdateStatus.UPDATE_IDLE_2 -> {
                    repository.setUpdateStatus(UpdateStatus.NOT_UPDATING)
                    needToSendPush = true
                }
            }
        }
        return needToSendPush
    }

}
