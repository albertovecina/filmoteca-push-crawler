package com.avs.filmoteca.data.ws

import retrofit2.http.GET
import retrofit2.http.Url
import rx.Observable

interface FilmotecaInterface {

    @get:GET("/es/webs-municipales/filmoteca/agenda/folder_listing")
    val moviesListHtmlObservable: Observable<String>

}
