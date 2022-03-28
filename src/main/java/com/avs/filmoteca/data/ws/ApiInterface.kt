package com.avs.filmoteca.data.ws

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @GET("movies")
    fun getStoredMovies(@Query("region") region: String): Call<List<String>>

    @GET("registrationId")
    fun getRegistrationIds(@Query("region") region: String): Call<List<String>>

    @POST("movies")
    fun updateMovies(@Body movies: List<String>, @Query("region") region: String): Call<Void>

}
