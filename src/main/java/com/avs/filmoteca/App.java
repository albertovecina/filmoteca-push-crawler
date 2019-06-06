package com.avs.filmoteca;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.avs.filmoteca.data.domain.UpdateStatus;
import com.avs.filmoteca.data.domain.push.PushMessage;
import com.avs.filmoteca.data.repository.DataRepository;

import rx.Observable;
import rx.Observer;

/**
 * Hello world!
 *
 */
public class App implements Observer<List<String>> {

	private DataRepository mRepository = DataRepository.getInstance();
	private List<String> mCurrentMovies;

	public static void main(String[] args) {
		App app = new App();
		app.init();

	}

	public void init() {

		System.out.println("Init");

		Observable
				.zip(mRepository.getStoredMoviesObservable(),
						mRepository.getPublishedMoviesObservable()
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
	}

	@Override
	public void onNext(List<String> addedMovies) {
		// TODO Auto-generated method stub
		addedMovies.forEach(System.out::println);
		boolean isUpdating = !addedMovies.isEmpty();
		if (isUpdating)
			updateMovies(mCurrentMovies);
		if (needToSendPush(isUpdating))
			sendPushNotification();
	}

	private void sendPushNotification() {
		mRepository.getRegistrationIdsObservable()
				.flatMap(registrationIds -> mRepository.getPushDeliveryObservable(new PushMessage.Builder()
						.setRegistrationIds(registrationIds).setTitleResId("notification_title_normal")
						.setMessageResId("notification_message_new_movies").setIconResId("ic_notification").build()))
				.toBlocking().subscribe();
	}

	private void updateMovies(List<String> movies) {
		mRepository.getUpdateMoviesObservable(movies).toBlocking().subscribe();
	}

	public boolean needToSendPush(boolean isUpdating) {
		boolean needToSendPush = false;
		if (isUpdating) {
			mRepository.setUpdateStatus(UpdateStatus.UPDATING);
		} else {
			switch (mRepository.getLastUpdateStatus()) {
			case UpdateStatus.UPDATING:
				mRepository.setUpdateStatus(UpdateStatus.UPDATE_IDLE_1);
				break;
			case UpdateStatus.UPDATE_IDLE_1:
				mRepository.setUpdateStatus(UpdateStatus.UPDATE_IDLE_2);
				break;
			case UpdateStatus.UPDATE_IDLE_2:
				mRepository.setUpdateStatus(UpdateStatus.NOT_UPDATING);
				needToSendPush = true;
			}
		}

		return needToSendPush;
	}
}
