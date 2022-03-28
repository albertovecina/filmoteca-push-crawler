package com.avs.filmoteca.domain.model.push

import com.google.gson.annotations.SerializedName

class PushMessage private constructor() {

    @SerializedName("registration_ids")
    var registrationIds: List<String>? = null
    var notification: Notification = Notification()

    class Builder {

        private val pushMessage: PushMessage = PushMessage()

        fun setRegistrationIds(registrationIds: List<String>): Builder {
            pushMessage.registrationIds = registrationIds
            return this
        }

        fun setTitle(title: String): Builder {
            pushMessage.notification.title = title
            pushMessage.notification.titleResId = ""
            return this
        }

        fun setTitleResId(titleResId: String): Builder {
            pushMessage.notification.title = ""
            pushMessage.notification.titleResId = titleResId
            return this
        }

        fun setMessage(message: String): Builder {
            pushMessage.notification.message = message
            pushMessage.notification.messageResId = ""
            return this
        }

        fun setMessageResId(messageResId: String): Builder {
            pushMessage.notification.message = ""
            pushMessage.notification.messageResId = messageResId
            return this
        }

        fun setIconResId(iconResId: String): Builder {
            pushMessage.notification.icon = iconResId
            return this
        }

        fun setSound(sound: String): Builder {
            pushMessage.notification.sound = sound
            return this
        }

        fun build(): PushMessage {
            return pushMessage
        }

    }

}
