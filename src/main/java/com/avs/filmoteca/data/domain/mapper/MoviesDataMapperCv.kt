package com.avs.filmoteca.data.domain.mapper

import com.avs.filmoteca.data.domain.Movie
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*

/**
 * Created by seldon on 26/03/15.
 */
class MoviesDataMapperCv : MoviesDataMapper {

    companion object {

        private const val CLASS_ACTIVITY = "actividad"
        private const val CLASS_MOVIE_TEXTS = "textos-peli"

        private const val CLASS_DAY = "dia"
        private const val CLASS_MONTH = "mes"
        private const val CLASS_HOUR = "hora"

        private const val TAG_A = "a"
        private const val ATTRIBUTE_TITLE_TITLE = "title"
        private const val ATTRIBUTE_HREF = "href"

        private const val TAG_P = "p"

    }

    override fun transformMovie(html: String?): List<Movie> {
        val moviesList = ArrayList<Movie>()

        if (!html.isNullOrEmpty()) {
            val document = Jsoup.parse(html)
            val activities = document.getElementsByClass(CLASS_ACTIVITY)

            activities.forEach { activity ->
                val movie = Movie()
                movie.date = getDate(activity)
                val movieTexts = activity.getElementsByClass(CLASS_MOVIE_TEXTS)
                movieTexts.forEach { movieText ->

                    val links = movieText.getElementsByTag(TAG_A)
                    if (links.size > 0) {
                        movie.title = links[0].attr(ATTRIBUTE_TITLE_TITLE)
                        movie.url = links[0].attr(ATTRIBUTE_HREF)
                    }

                    movie.subtitle = getSubtitle(movieText)

                }
                moviesList.add(movie)
            }
        }
        return moviesList
    }


    private fun getDate(activity: Element): String {
        val day = activity.getElementsByClass(CLASS_DAY)[0].text()
        val month = activity.getElementsByClass(CLASS_MONTH)[0].text()
        val hour = activity.getElementsByClass(CLASS_HOUR)[0].text()

        return "$day/$month - $hour"
    }

    private fun getSubtitle(movieText: Element): String {
        val paragraphs = movieText.getElementsByTag(TAG_P)
        return if (paragraphs.size > 0)
            paragraphs[0].text()
        else
            ""
    }

}
