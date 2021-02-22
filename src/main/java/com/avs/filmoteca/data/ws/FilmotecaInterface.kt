package com.avs.filmoteca.data.ws

import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable

interface FilmotecaInterface {

    @GET
    fun getMoviesListHtmlObservable(@Url url: String): Observable<String?>

}
