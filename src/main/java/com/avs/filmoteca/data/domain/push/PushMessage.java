package com.avs.filmoteca.data.domain.push;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PushMessage {

	@SerializedName("registration_ids")
	private List<String> registrationIds;
	private Notification notification;

	private PushMessage() {
		super();
	}

	public List<String> getRegistrationIds() {
		return registrationIds;
	}

	public void setRegistrationIds(List<String> registrationIds) {
		this.registrationIds = registrationIds;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public static class Builder {

		private PushMessage pushMessage;

		public Builder() {
			super();
			pushMessage = new PushMessage();
			pushMessage.setNotification(new Notification("Title", "Body", "ic_launcher", "default"));
		}

		public Builder setRegistrationIds(List<String> registrationIds) {
			pushMessage.setRegistrationIds(registrationIds);
			return this;
		}

		public Builder setTitle(String title) {
			pushMessage.getNotification().setTitle(title);
			pushMessage.getNotification().setTitleResId(null);
			return this;
		}

		public Builder setTitleResId(String titleResId) {
			pushMessage.getNotification().setTitle(null);
			pushMessage.getNotification().setTitleResId(titleResId);
			return this;
		}

		public Builder setMessage(String message) {
			pushMessage.getNotification().setMessage(message);
			pushMessage.getNotification().setMessageResId(null);
			return this;
		}

		public Builder setMessageResId(String messageResId) {
			pushMessage.getNotification().setMessage(null);
			pushMessage.getNotification().setMessageResId(messageResId);
			return this;
		}

		public Builder setIconResId(String iconResId) {
			pushMessage.getNotification().setIcon(iconResId);
			return this;
		}

		public Builder setSound(String sound) {
			pushMessage.getNotification().setSound(sound);
			return this;
		}

		public PushMessage build() {
			return pushMessage;
		}

	}

}
