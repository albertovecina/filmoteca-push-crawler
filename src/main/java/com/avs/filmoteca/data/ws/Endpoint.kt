package com.avs.filmoteca.data.ws

import com.avs.filmoteca.data.Environment

sealed class Endpoint(val endpoint: String) {
    object Web : Endpoint("http://localhost/")
    object Service : Endpoint(Environment.EndpointWs.value)
    object Firebase : Endpoint("https://fcm.googleapis.com/")
}