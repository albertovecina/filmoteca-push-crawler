package com.avs.filmoteca.data.ws

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FilmotecaInterface {

    @GET
    fun getMoviesListHtml(@Url url: String): Call<String>

}
