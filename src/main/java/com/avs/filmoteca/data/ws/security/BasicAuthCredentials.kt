package com.avs.filmoteca.data.ws.security

import com.avs.filmoteca.data.domain.Environment

object BasicAuthCredentials {
    val user: String = Environment.BasicAuthUser.value
    val password: String = Environment.BasicAuthPassword.value
}