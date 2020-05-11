package com.automation.api.request.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserData implements Variables {

	@SerializedName("input")
	@Expose
	private UpdateUserInput input;

	public UpdateUserInput getInput() {
		return input;
	}

	public void setInput(UpdateUserInput input) {
		this.input = input;
	}

}