package com.avs.filmoteca.data.ws

sealed class Environment(val endpoint: String) {
    object WEB : Environment("http://localhost/")
    object BACKEND : Environment("http://seldon-nas.dnset.com:7070/filmoteca-ws/pro/")
    object FIREBASE : Environment("https://fcm.googleapis.com/")
}