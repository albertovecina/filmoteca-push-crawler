package com.avs.filmoteca.data.ws

import com.avs.filmoteca.domain.model.push.PushMessage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMInterface {

    @Headers("Content-Type:application/json")
    @POST("fcm/send")
    fun sendPush(@Header("Authorization") firebaseAuthorization: String, @Body message: PushMessage): Call<ResponseBody>

}
