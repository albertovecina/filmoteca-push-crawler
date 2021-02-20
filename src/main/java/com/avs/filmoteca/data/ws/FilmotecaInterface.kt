package com.avs.filmoteca.data.ws

import retrofit2.http.GET
import rx.Observable

interface FilmotecaInterface {

    @GET("/es/webs-municipales/filmoteca/agenda/folder_listing")
    fun getMoviesListHtmlObservable(): Observable<String>

}
