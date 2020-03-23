package com.automation.env.pojo;

public class Env {

	private String name;

	private WebDetails webDetails;

	private ApiDetails apiDetails;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WebDetails getWebDetails() {
		return webDetails;
	}

	public void setWebDetails(WebDetails webDetails) {
		this.webDetails = webDetails;
	}

	public ApiDetails getApiDetails() {
		return apiDetails;
	}

	public void setApiDetails(ApiDetails apiDetails) {
		this.apiDetails = apiDetails;
	}

}