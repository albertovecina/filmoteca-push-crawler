package com.avs.filmoteca.data.ws;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

	public static FilmotecaInterface getInstance() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(Environment.BASE_URL)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.build();

		return retrofit.create(FilmotecaInterface.class);
	}

}
