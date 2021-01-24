package com.avs.filmoteca.data.ws

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {

    val filmotecaInterface: FilmotecaInterface by lazy {
        getRetrofit(Environment.WEB).create(FilmotecaInterface::class.java)
    }

    val backendInterface: BackendInterface by lazy {
        getRetrofit(Environment.BACKEND).create(BackendInterface::class.java)
    }

    val fcmInterface: FCMInterface by lazy {
        getRetrofit(Environment.FIREBASE).create(FCMInterface::class.java)
    }

    private val basicAuthHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor())
            .build()
    }

    private fun getRetrofit(environment: Environment): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(environment.endpoint)

            if (environment == Environment.BACKEND)
                client(basicAuthHttpClient)

            addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            addConverterFactory(ScalarsConverterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
        }.build()

}
