package com.avs.filmoteca.data.ws

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

interface BackendInterface {

    @GET("movies")
    fun getStoredMoviesObservable(@Query("region") region: String): Observable<List<String>>

    @GET("registrationId")
    fun getRegistrationIdsObservable(@Query("region") region: String): Observable<List<String>>

    @POST("movies")
    fun getUpdateMoviesObservable(@Body movies: List<String>, @Query("region") region: String): Observable<Void>

}
