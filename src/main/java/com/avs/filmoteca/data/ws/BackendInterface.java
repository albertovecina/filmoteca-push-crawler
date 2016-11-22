package com.avs.filmoteca.data.ws;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface BackendInterface {
	
	@POST("/filmoteca-ws/movies")
	Observable<Void> getUpdateMoviesObservable(@Body List<String> movies);

	@GET("/filmoteca-ws/movies")
	Observable<List<String>> getStoredMoviesObservable();
	
	@GET("/filmoteca-ws/registrationId")
	Observable<List<String>> getRegistrationIdsObservable();

}
