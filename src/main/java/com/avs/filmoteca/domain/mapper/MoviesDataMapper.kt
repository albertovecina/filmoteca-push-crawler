package com.avs.filmoteca.domain.mapper

import com.avs.filmoteca.domain.model.Movie
import com.avs.filmoteca.domain.model.Region

interface MoviesDataMapper {

    companion object {
        fun newInstance(region: Region) = when (region) {
            Region.Albacete -> MoviesDataMapperAb()
            Region.ComunidadValenciana -> MoviesDataMapperCv()
        }
    }

    fun map(html: String?): List<Movie>

}