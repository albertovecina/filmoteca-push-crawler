package com.avs.filmoteca.data.ws;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

	public static FilmotecaInterface getFilmotecaInterface() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Environment.FILMOTECA_URL)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.build();

		return retrofit.create(FilmotecaInterface.class);
	}
	
	public static BackendInterface getBackendInterface() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Environment.BACKEND_URL)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		return retrofit.create(BackendInterface.class);
	}

}
