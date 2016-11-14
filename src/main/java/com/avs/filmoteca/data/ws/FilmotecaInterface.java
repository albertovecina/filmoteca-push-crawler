package com.avs.filmoteca.data.ws;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface FilmotecaInterface {
	
	@GET("/es/webs-municipales/filmoteca/agenda/folder_listing")
	Observable<String> moviesListHtml();
	
}
