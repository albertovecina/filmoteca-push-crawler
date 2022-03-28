package com.avs.filmoteca.data

sealed class Environment(val name: String, val value: String = System.getenv(name)) {

    object BasicAuthUser : Environment("BASIC_AUTH_USER")
    object BasicAuthPassword : Environment("BASIC_AUTH_PASSWORD")
    object FirebaseApiKey : Environment("FIREBASE_API_KEY")
    object EndpointWs : Environment("ENDPOINT_WS")

}