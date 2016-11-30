package com.avs.filmoteca.data.repository;

import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.avs.filmoteca.data.domain.Movie;
import com.avs.filmoteca.data.domain.UpdateStatus;
import com.avs.filmoteca.data.domain.mapper.MoviesDataMapper;
import com.avs.filmoteca.data.domain.push.PushMessage;
import com.avs.filmoteca.data.ws.ApiClient;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.schedulers.Schedulers;

public class DataRepository {

	private static DataRepository sDataRepository;

	private static Preferences sPreferences;

	public static DataRepository getInstance() {
		if (sDataRepository == null)
			sDataRepository = new DataRepository();
		return sDataRepository;
	}

	private DataRepository() {
		sPreferences = Preferences.userNodeForPackage(DataRepository.class);
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

	public Observable<ResponseBody> getPushDeliveryObservable(PushMessage message) {
		return ApiClient.getFCMInterface()
				.getPushDeliveryObservable("key=" + System.getenv("FIREBASE_API_KEY"), message)
				.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline());
	}

	public void setUpdating(boolean updating) {
		sPreferences.putBoolean(UpdateStatus.PREFERENCE_IS_UPDATING, updating);
		try {
			sPreferences.sync();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isUpdating() {
		return sPreferences.getBoolean(UpdateStatus.PREFERENCE_IS_UPDATING, false);
	}

}
