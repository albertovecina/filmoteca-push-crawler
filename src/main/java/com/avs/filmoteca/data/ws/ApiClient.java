package com.avs.filmoteca.data.ws;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

	public static FilmotecaInterface getFilmotecaInterface() {
		return getRetrofit(Environment.FILMOTECA_URL).create(FilmotecaInterface.class);
	}

	public static BackendInterface getBackendInterface() {
		return getRetrofit(Environment.BACKEND_URL).create(BackendInterface.class);
	}

	public static FCMInterface getFCMInterface() {
		return getRetrofit(Environment.FIREBASE_URL).create(FCMInterface.class);
	}

	private static Retrofit getRetrofit(String url) {
		return new Retrofit.Builder().baseUrl(url).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(GsonConverterFactory.create()).build();
	}

}
