package com.avs.filmoteca.data.domain.push;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Notification {

	@SerializedName("title")
	private String title;
	@SerializedName("title_loc_key")
	private String titleResId;
	@SerializedName("body")
	private String message;
	@SerializedName("body_loc_key")
	private String messageResId;
	private String icon;
	private String sound;

	public Notification() {
		super();
	}

	public Notification(String title, String message, String icon, String sound) {
		super();
		this.title = title;
		this.message = message;
		this.icon = icon;
		this.sound = sound;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleResId() {
		return titleResId;
	}

	public void setTitleResId(String titleResId) {
		this.titleResId = titleResId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageResId() {
		return messageResId;
	}

	public void setMessageResId(String messageResId) {
		this.messageResId = messageResId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

}
