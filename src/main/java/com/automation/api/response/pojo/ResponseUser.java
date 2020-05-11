package com.automation.api.response.pojo;

import com.automation.api.pojo.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUser {

	@SerializedName("ok")
	@Expose
	private Boolean ok;
	@SerializedName("error")
	@Expose
	private String error;
	@SerializedName("user")
	@Expose
	private User user;

	public Boolean getOk() {
		return ok;
	}

	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}