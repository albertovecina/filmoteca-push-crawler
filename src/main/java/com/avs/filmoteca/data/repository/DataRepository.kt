package com.avs.filmoteca.data.repository

import com.avs.filmoteca.data.domain.Movie
import com.avs.filmoteca.data.domain.UpdateStatus
import com.avs.filmoteca.data.domain.mapper.MoviesDataMapper
import com.avs.filmoteca.data.domain.push.PushMessage
import com.avs.filmoteca.data.ws.ApiClient
import okhttp3.ResponseBody
import rx.Observable
import rx.schedulers.Schedulers
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

class DataRepository private constructor() {

    companion object {


        private var preferences: Preferences = Preferences.userNodeForPackage(DataRepository::class.java)

        val instance: DataRepository by lazy { DataRepository() }

    }

    fun getPublishedMoviesObservable(): Observable<List<Movie>> =
            ApiClient.filmotecaInterface.moviesListHtmlObservable.map { MoviesDataMapper.transformMovie(it) }
                    .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline())

    fun getPublishedMoviesObservableMock(withMovies: Boolean): Observable<List<Movie>> = Observable.create { t ->
        if (withMovies)
            t?.onNext(MoviesDataMapper.transformMovie(MockData.MOVIE_LIST))
        else
            t?.onNext(ArrayList())
        t?.onCompleted()
    }

    fun getStoredMoviesObservable(): Observable<List<String>> =
            ApiClient.backendInterface.getStoredMoviesObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.trampoline())

    fun getRegistrationIdsObservable(): Observable<List<String>> =
            ApiClient.backendInterface.getRegistrationIdsObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.trampoline())

    fun getLastUpdateStatus(): Int = preferences.getInt(UpdateStatus.PREFERENCE_UPDATE_STATUS, UpdateStatus.NOT_UPDATING)


    fun getUpdateMoviesObservable(movieTitles: List<String>): Observable<Void> {
        return ApiClient.backendInterface.getUpdateMoviesObservable(movieTitles)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.trampoline())
    }

    fun getPushDeliveryObservable(registrationIds: List<String>): Observable<Unit> {
        val groupedRegistrationIds: List<List<String>> =
                registrationIds.groupBy { registrationIds.indexOf(it) / 1000 }.values.toList()
        val observableBatch: List<Observable<ResponseBody>> = groupedRegistrationIds.map {
            createPushObservable(PushMessage.Builder()
                    .setRegistrationIds(it)
                    .setTitleResId("notification_title_normal")
                    .setMessageResId("notification_message_new_movies")
                    .setIconResId("ic_notification").build())
        }
        return Observable.zip(observableBatch) {}
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.trampoline())
    }

    private fun createPushObservable(message: PushMessage): Observable<ResponseBody> =
            ApiClient.fcmInterface
                    .getPushDeliveryObservable("key=" + System.getenv("FIREBASE_API_KEY"), message)

    fun setUpdateStatus(status: Int) {
        preferences.putInt(UpdateStatus.PREFERENCE_UPDATE_STATUS, status)
        try {
            preferences.sync()
        } catch (e: BackingStoreException) {
            e.printStackTrace()
        }

    }

}
