package com.avs.filmoteca.data.domain.mapper

import com.avs.filmoteca.data.domain.Movie
import com.avs.filmoteca.data.domain.Region

interface MoviesDataMapper {

    companion object {
        fun newInstance(region: Region) = when (region) {
            Region.Albacete -> MoviesDataMapperAb()
            Region.ComunidadValenciana -> MoviesDataMapperCv()
        }
    }

    fun transformMovie(html: String?): List<Movie>

}