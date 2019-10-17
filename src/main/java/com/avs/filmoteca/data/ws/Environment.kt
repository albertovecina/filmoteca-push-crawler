package com.avs.filmoteca.data.ws

object Environment {

    val FILMOTECA_URL = System.getenv("URL_FILMOTECA_WEB") ?: "http://www.albacete.es/"
    val BACKEND_URL = System.getenv("URL_FILMOTECA_API") ?: "http://seldon-nas.dnset.com:8080/filmoteca-ws/pro/"
    val FIREBASE_URL = System.getenv("URL_GOOGLE_API") ?: "https://fcm.googleapis.com/"

}
