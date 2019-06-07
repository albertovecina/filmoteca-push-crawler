package com.avs.filmoteca.data.ws;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface BackendInterface {
	
	@POST("movies")
	Observable<Void> getUpdateMoviesObservable(@Body List<String> movies);

	@GET("movies")
	Observable<List<String>> getStoredMoviesObservable();
	
	@GET("registrationId")
	Observable<List<String>> getRegistrationIdsObservable();

}
