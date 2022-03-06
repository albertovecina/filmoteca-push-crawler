package com.avs.filmoteca.data.domain

sealed class Environment(val name: String) {

    val value: String
        get() = System.getenv(name)

    object BasicAuthUser : Environment("BASIC_AUTH_USER")
    object BasicAuthPassword : Environment("BASIC_AUTH_PASSWORD")
    object FirebaseApiKey : Environment("FIREBASE_API_KEY")
    object EndpointWs : Environment("EndpointWs")

}