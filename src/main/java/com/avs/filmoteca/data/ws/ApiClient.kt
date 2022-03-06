package com.avs.filmoteca.data.ws

import com.avs.filmoteca.data.ws.security.BasicAuthCredentials
import com.avs.filmoteca.data.ws.security.BasicAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {

    val filmotecaInterface: FilmotecaInterface by lazy {
        getRetrofit(Endpoint.Web).create(FilmotecaInterface::class.java)
    }

    val backendInterface: BackendInterface by lazy {
        getRetrofit(Endpoint.Service).create(BackendInterface::class.java)
    }

    val fcmInterface: FCMInterface by lazy {
        getRetrofit(Endpoint.Firebase).create(FCMInterface::class.java)
    }

    private val basicAuthHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(BasicAuthCredentials.user, BasicAuthCredentials.password))
            .build()
    }

    private fun getRetrofit(endpoint: Endpoint): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(endpoint.endpoint)

            if (endpoint == Endpoint.Service)
                client(basicAuthHttpClient)

            addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            addConverterFactory(ScalarsConverterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
        }.build()

}
