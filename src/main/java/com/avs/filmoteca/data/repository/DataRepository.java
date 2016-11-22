package com.avs.filmoteca.data.repository;

import java.util.List;

import com.avs.filmoteca.data.domain.Movie;
import com.avs.filmoteca.data.domain.mapper.MoviesDataMapper;
import com.avs.filmoteca.data.ws.ApiClient;

import rx.Observable;
import rx.schedulers.Schedulers;

public class DataRepository {

	private static DataRepository sDataRepository;

	public static DataRepository getInstance() {
		if (sDataRepository == null)
			sDataRepository = new DataRepository();
		return sDataRepository;
	}

	private DataRepository() {

	}

	public Observable<List<Movie>> getPublishedMoviesObservable() {
		return ApiClient.getFilmotecaInterface().getMoviesListHtmlObservable().map(MoviesDataMapper::transformMovie)
				.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline());
	}

	public Observable<Void> getUpdateMoviesObservable(List<String> movieTitles) {
		return ApiClient.getBackendInterface().getUpdateMoviesObservable(movieTitles)
				.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline());
	}

	public Observable<List<String>> getStoredMoviesObservable() {
		return ApiClient.getBackendInterface().getStoredMoviesObservable().subscribeOn(Schedulers.newThread())
				.observeOn(Schedulers.trampoline());
	}

	public Observable<List<String>> getRegistrationIdsObservable() {
		return ApiClient.getBackendInterface().getRegistrationIdsObservable().subscribeOn(Schedulers.newThread())
				.observeOn(Schedulers.trampoline());
	}

}
