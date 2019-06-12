package com.avs.filmoteca.data.ws

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {

    val filmotecaInterface: FilmotecaInterface
        get() = getRetrofit(Environment.FILMOTECA_URL).create(FilmotecaInterface::class.java)

    val backendInterface: BackendInterface
        get() = getRetrofit(Environment.BACKEND_URL).create(BackendInterface::class.java)

    val fcmInterface: FCMInterface
        get() = getRetrofit(Environment.FIREBASE_URL).create(FCMInterface::class.java)

    private fun getRetrofit(url: String): Retrofit {
        return Retrofit.Builder().baseUrl(url).addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build()
    }

}
