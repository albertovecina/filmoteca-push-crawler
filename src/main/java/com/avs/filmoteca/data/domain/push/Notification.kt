package com.avs.filmoteca.data.domain.push

import com.google.gson.annotations.SerializedName

data class Notification(
        @SerializedName("title")
        var title: String = "",
        @SerializedName("title_loc_key")
        var titleResId: String = "",
        @SerializedName("body")
        var message: String = "",
        @SerializedName("body_loc_key")
        var messageResId: String = "",
        var icon: String = "ic_launcher",
        var sound: String = "default"
)
