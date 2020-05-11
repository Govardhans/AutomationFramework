package com.automation.api.response.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedUserResponse {

	@SerializedName("data")
	@Expose
	private CreatedUserData data;

	public CreatedUserData getData() {
		return data;
	}

	public void setData(CreatedUserData data) {
		this.data = data;
	}

}