package com.automation.api.response.pojo;

import java.util.List;

import com.automation.api.pojo.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUsersData {

	@SerializedName("users")
	@Expose
	private List<User> users = null;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}