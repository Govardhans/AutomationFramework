package com.automation.api.response.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedUserData {

	@SerializedName("createUser")
	@Expose
	private ResponseUser updateUser;

	public ResponseUser getResponseUser() {
		return updateUser;
	}

	public void setResponseUser(ResponseUser updateUser) {
		this.updateUser = updateUser;
	}

}