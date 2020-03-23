package com.automation.api.response.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatedUserResponse {

	@SerializedName("data")
	@Expose
	private UpdatedUserData data;

	public UpdatedUserData getData() {
		return data;
	}

	public void setData(UpdatedUserData data) {
		this.data = data;
	}

}