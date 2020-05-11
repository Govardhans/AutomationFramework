package com.automation.api.request.data;

import com.automation.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**<p>
 * <code>UserQueryVariable</code> is used to create user payload
 * </p>
 * @author Govi
 *
 */
public class UserQueryVariable implements QueryVariables {

	@SerializedName("input")
	@Expose
	private User input;

	public User getInput() {
		return input;
	}

	public void setInput(User input) {
		this.input = input;
	}

}