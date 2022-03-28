package com.avs.filmoteca.domain.model

sealed class Region(val code: String, val endpoint: String) {

    companion object {
        fun fromCode(code: String): Region = when (code) {
            "ab" -> Albacete
            "cv" -> ComunidadValenciana
            else -> Albacete
        }
    }

    object Albacete : Region("ab", "http://www.albacete.es/es/webs-municipales/filmoteca/agenda/folder_listing")
    object ComunidadValenciana :
            Region("cv", "https://ivc.gva.es/es/audiovisuales/programacion/valencia-la-filmoteca-cas")

    override fun toString() = code

}