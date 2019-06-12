package com.avs.filmoteca.data.repository

import com.avs.filmoteca.data.domain.Movie
import com.avs.filmoteca.data.domain.UpdateStatus
import com.avs.filmoteca.data.domain.mapper.MoviesDataMapper
import com.avs.filmoteca.data.domain.push.PushMessage
import com.avs.filmoteca.data.ws.ApiClient
import okhttp3.ResponseBody
import rx.Observable
import rx.Subscriber
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

    fun getPublishedMoviesObservableMock(withMovies: Boolean): Observable<List<Movie>> = Observable.create(object : Observable.OnSubscribe<List<Movie>> {
        override fun call(t: Subscriber<in List<Movie>>?) {
            if (withMovies)
                t?.onNext(MoviesDataMapper.transformMovie(MockData.MOVIE_LIST))
            else
                t?.onNext(ArrayList())
            t?.onCompleted()
        }
    })

    fun getStoredMoviesObservable(): Observable<List<String>> =
            ApiClient.backendInterface.getStoredMoviesObservable().subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.trampoline())

    fun getRegistrationIdsObservable(): Observable<List<String>> =
            ApiClient.backendInterface.getRegistrationIdsObservable().subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.trampoline())

    fun getLastUpdateStatus(): Int = preferences.getInt(UpdateStatus.PREFERENCE_UPDATE_STATUS, UpdateStatus.NOT_UPDATING)


    fun getUpdateMoviesObservable(movieTitles: List<String>): Observable<Void> {
        return ApiClient.backendInterface.getUpdateMoviesObservable(movieTitles)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline())
    }

    fun getPushDeliveryObservable(message: PushMessage): Observable<ResponseBody> {
        return ApiClient.fcmInterface
                .getPushDeliveryObservable("key=" + System.getenv("FIREBASE_API_KEY"), message)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline())
    }

    fun setUpdateStatus(status: Int) {
        preferences.putInt(UpdateStatus.PREFERENCE_UPDATE_STATUS, status)
        try {
            preferences.sync()
        } catch (e: BackingStoreException) {
            e.printStackTrace()
        }

    }

}
