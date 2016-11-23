package com.avs.filmoteca.data.ws;

import com.avs.filmoteca.data.domain.push.PushMessage;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface FCMInterface {
	
	
	@Headers("Content-Type:application/json")
	@POST("/fcm/send")
	Observable<ResponseBody> getPushDeliveryObservable(@Header("Authorization") String firebaseAuthorization, @Body PushMessage message);

}
