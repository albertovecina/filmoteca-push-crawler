package com.avs.filmoteca.data.ws.security

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(user: String, password: String) : Interceptor {
    init {
        if (user.isEmpty() || password.isEmpty())
            throw Exception("Set the environment variables BASIC_AUTH_USER and BASIC_AUTH_PASSWORD before execution.")
    }

    private val credentials: String = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain
            .request()
            .newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

}