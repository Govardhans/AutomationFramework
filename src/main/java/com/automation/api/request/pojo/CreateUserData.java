package com.automation.api.request.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateUserData implements Variables {

	@SerializedName("input")
	@Expose
	private CreateUserInput input;

	public CreateUserInput getInput() {
		return input;
	}

	public void setInput(CreateUserInput input) {
		this.input = input;
	}

}