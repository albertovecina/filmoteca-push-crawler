package com.avs.filmoteca

import com.avs.filmoteca.data.domain.Region
import com.avs.filmoteca.data.domain.UpdateStatus
import com.avs.filmoteca.data.repository.DataRepository

class App(private val region: Region = Region.Albacete) {

    private val repository = DataRepository.instance

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
        println("INIT")
        println("Region: $region")
        substractNewMovies(repository.getStoredMovies(region), repository.getPublishedMovies(region)).let { newMovies ->
            newMovies.forEach { println(it) }
            val isUpdating = newMovies.isNotEmpty()
            if (isUpdating)
                updateMovies(newMovies)
            if (needToSendPush(isUpdating))
                sendPushNotification()
        }
        println("END")
    }

    private fun substractNewMovies(oldMovies: List<String>?, newMovies: List<String>?): List<String> =
            if (oldMovies != null && newMovies != null)
                newMovies.filter { !oldMovies.contains(it) }
            else
                ArrayList()

    private fun sendPushNotification() {
        repository.sendPush(repository.getRegistrationIds(region))
        println("PUSH SEND")
    }

    private fun updateMovies(movies: List<String>) {
        repository.updateMovies(region, movies)
    }

    private fun needToSendPush(newMoviesAvailable: Boolean): Boolean {
        var needToSendPush = false
        if (newMoviesAvailable) {
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
        println("UPDATE STATUS: ${repository.getLastUpdateStatus(region)} NEED PUSH: $needToSendPush")
        return needToSendPush
    }

}
