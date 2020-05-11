package com.automation.api.response.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUsersResponse {

	@SerializedName("data")
	@Expose
	private GetUsersData data;

	public GetUsersData getData() {
		return data;
	}

	public void setData(GetUsersData data) {
		this.data = data;
	}

}