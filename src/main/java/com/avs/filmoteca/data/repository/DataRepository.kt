package com.avs.filmoteca.data.repository

import com.avs.filmoteca.data.Environment
import com.avs.filmoteca.domain.model.Region
import com.avs.filmoteca.domain.model.UpdateStatus
import com.avs.filmoteca.domain.mapper.MoviesDataMapper
import com.avs.filmoteca.domain.model.push.PushMessage
import com.avs.filmoteca.data.ws.ApiClient
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

class DataRepository private constructor() {

    companion object {

        private var preferences: Preferences = Preferences.userNodeForPackage(DataRepository::class.java)

        val instance: DataRepository by lazy { DataRepository() }

    }

    fun getPublishedMovies(region: Region): List<String> =
            ApiClient.filmotecaInterface.getMoviesListHtml(region.endpoint).execute().body()?.let { html ->
                MoviesDataMapper.newInstance(region).map(html).map { it.title }
            } ?: emptyList()

    fun getStoredMovies(region: Region): List<String> =
            ApiClient.apiInterface.getStoredMovies(region.code).execute().body() ?: emptyList()

    fun getRegistrationIds(region: Region): List<String> =
            ApiClient.apiInterface.getRegistrationIds(region.code).execute().body() ?: emptyList()

    fun updateMovies(region: Region, movieTitles: List<String>) {
        ApiClient.apiInterface.updateMovies(movieTitles, region.code).execute()
    }

    fun sendPush(registrationIds: List<String>) {
        val groupedRegistrationIds: List<List<String>> =
                registrationIds.groupBy { registrationIds.indexOf(it) / 1000 }.values.toList()
        groupedRegistrationIds.forEach {
            val message = PushMessage.Builder()
                    .setRegistrationIds(it)
                    .setTitleResId("notification_title_normal")
                    .setMessageResId("notification_message_new_movies")
                    .setIconResId("ic_notification").build()
            ApiClient.fcmInterface.sendPush("key=" + Environment.FirebaseApiKey.value, message).execute()
        }
    }

    fun getLastUpdateStatus(region: Region): Int =
            preferences.getInt("${UpdateStatus.PREFERENCE_UPDATE_STATUS}_${region.code}", UpdateStatus.NOT_UPDATING)

    fun setUpdateStatus(region: Region, status: Int) {
        preferences.putInt("${UpdateStatus.PREFERENCE_UPDATE_STATUS}_${region.code}", status)
        try {
            preferences.sync()
        } catch (e: BackingStoreException) {
            e.printStackTrace()
        }
    }

}
