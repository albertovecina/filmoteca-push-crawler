package com.avs.filmoteca.data.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.avs.filmoteca.data.domain.Movie;

public class MoviesDataMapper {
	private static final String CLASS_EVENT = "contenttype-evento";
	public static final String CLASS_DATE = "description";

	public static List<Movie> transformMovie(String html) {
		List<Movie> moviesList = new ArrayList<Movie>();

		if (html == null || html.isEmpty())
			return moviesList;

		Document document = Jsoup.parse(html);
		Elements events = document.getElementsByClass(CLASS_EVENT);
		Elements dates = document.getElementsByClass(CLASS_DATE);

		Movie movie;

		for (int x = 0; x < events.size(); x++) {
			Element event = events.get(x);
			movie = new Movie();
			Element link = event.getElementsByClass("url").first();
			String title = link.text();
			if (title.indexOf("(") > 0) {
				movie.setTitle(title.substring(0, title.indexOf("(")).trim());
				movie.setSubtitle(title.substring(title.indexOf("(")));
			} else {
				movie.setTitle(title.trim());
				movie.setSubtitle("");
			}

			movie.setUrl(link.attr("href"));
			String date = "- " + dates.get(x).text();
			movie.setDate(date);

			moviesList.add(movie);
		}

		return moviesList;
	}
}
