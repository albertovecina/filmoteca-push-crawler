package com.avs.filmoteca.data.ws

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rx.Observable

interface BackendInterface {

    @GET("movies")
    fun getStoredMoviesObservable(): Observable<List<String>>

    @GET("registrationId")
    fun getRegistrationIdsObservable(): Observable<List<String>>

    @POST("movies")
    fun getUpdateMoviesObservable(@Body movies: List<String>): Observable<Void>

}
