package com.avs.filmoteca.data.ws

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {

    private val credentials: String = Credentials.basic("", "")

    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain
            .request()
            .newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}