package com.avs.filmoteca;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.avs.filmoteca.data.repository.DataRepository;

import rx.Observable;
import rx.Observer;

/**
 * Hello world!
 *
 */
public class App implements Observer<List<String>> {

	private List<String> mCurrentMovies;

	public static void main(String[] args) {
		App app = new App();
		app.init();

	}

	public void init() {
		System.out.println("Init");

		Observable.zip(DataRepository.getInstance().getStoredMoviesObservable(),
				DataRepository.getInstance()
						.getPublishedMoviesObservable().map(movies -> mCurrentMovies = movies.stream()
								.map(movie -> movie.getTitle()).collect(Collectors.toList())),
				this::substractNewMovies).subscribe(this);
		
		DataRepository.getInstance().getRegistrationIdsObservable().subscribe(registrationIds -> registrationIds.forEach(System.out::println));

		while (true) {
		}
	}

	public List<String> substractNewMovies(List<String> oldMovies, List<String> newMovies) {
		if (oldMovies != null && newMovies != null) {
			List<String> addedMovies = newMovies.stream().filter(newMovie -> !oldMovies.contains(newMovie))
					.collect(Collectors.toList());
			return addedMovies;
		}
		return new ArrayList<>();
	}

	@Override
	public void onCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNext(List<String> addedMovies) {
		// TODO Auto-generated method stub
		addedMovies.forEach(System.out::println);
		if (!addedMovies.isEmpty())
			DataRepository.getInstance().getUpdateMoviesObservable(mCurrentMovies).subscribe();
	}
}
