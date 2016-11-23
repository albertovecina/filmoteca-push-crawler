package com.avs.filmoteca;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.avs.filmoteca.data.domain.push.PushMessage;
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

		Observable
				.zip(DataRepository.getInstance().getStoredMoviesObservable(),
						DataRepository.getInstance().getPublishedMoviesObservable()
								.map(movies -> mCurrentMovies = movies.stream().map(movie -> movie.getTitle())
										.collect(Collectors.toList())),
						this::substractNewMovies)
				.toBlocking().subscribe(this);

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
		System.out.println("Completed");
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
		;
	}

	@Override
	public void onNext(List<String> addedMovies) {
		// TODO Auto-generated method stub
		addedMovies.forEach(System.out::println);
		if (!addedMovies.isEmpty()) {
			DataRepository.getInstance().getUpdateMoviesObservable(mCurrentMovies).toBlocking().subscribe();
			DataRepository.getInstance().getRegistrationIdsObservable()
					.flatMap(registrationIds -> DataRepository.getInstance()
							.getPushDeliveryObservable(new PushMessage.Builder().setRegistrationIds(registrationIds)
									.setTitleResId("notification_title").setMessageResId("notification_message")
									.setIconResId("ic_movie").build()))
					.toBlocking().subscribe();
		}
	}
}
