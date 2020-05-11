package com.automation.api.request.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateUserInput {

	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("lastName")
	@Expose
	private String lastName;
	@SerializedName("newsletter")
	@Expose
	private Boolean newsletter;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Boolean newsletter) {
		this.newsletter = newsletter;
	}

	@Override
	public String toString() {
		return "CreateUserInput [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", newsletter=" + newsletter + "]";
	}
	
}