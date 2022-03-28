package com.avs.filmoteca

import com.avs.filmoteca.core.extensions.toJson
import com.avs.filmoteca.core.util.Logger
import com.avs.filmoteca.data.repository.DataRepository
import com.avs.filmoteca.domain.model.Region
import com.avs.filmoteca.domain.model.UpdateStatus

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
        Logger.logger.info("INIT - REGION: $region")
        repository.getPublishedMovies(region).let { publishedMovies ->
            repository.getStoredMovies(region).let { storedMovies ->
                publishedMovies.filter { !storedMovies.contains(it) }.let { newMovies ->
                    Logger.logger.info("PUBLISHED MOVIES:\n${publishedMovies.toJson()}\nSTORED MOVIES:\n${storedMovies.toJson()}\nNEW MOVIES:\n${newMovies.toJson()} \n REGION: $region")
                    val isUpdating = newMovies.isNotEmpty()
                    if (isUpdating)
                        repository.updateMovies(region, publishedMovies)
                    if (needToSendPush(isUpdating))
                        sendPushNotification()
                }
            }
        }
        Logger.logger.info("END - REGION: $region")
    }

    private fun sendPushNotification() {
        repository.sendPush(repository.getRegistrationIds(region))
        Logger.logger.info("PUSH SENT - REGION: $region")
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
        Logger.logger.info("UPDATE STATUS: ${repository.getLastUpdateStatus(region)} NEED PUSH: $needToSendPush - REGION $region")
        return needToSendPush
    }

}
