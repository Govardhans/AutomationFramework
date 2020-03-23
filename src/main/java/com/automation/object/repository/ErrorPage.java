package com.automation.object.repository;

import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class ErrorPage extends BasePage  implements Page{
	
	private SelenideElement homeButton;
	private SelenideElement errorMsg;
	public ErrorPage() {
		init();	
	}
	
	public void init() {
		errorMsg = $(By.xpath("//ng-component//p"));
		homeButton = $(By.xpath("//ng-component//button"));
	}

	/**
	 * @return the homeButton
	 */
	public SelenideElement getHomeButton() {
		return homeButton;
	}

	/**
	 * @param homeButton the homeButton to set
	 */
	public void setHomeButton(SelenideElement homeButton) {
		this.homeButton = homeButton;
	}

	/**
	 * @return the errorMsg
	 */
	public SelenideElement getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param errorMsg the errorMsg to set
	 */
	public void setErrorMsg(SelenideElement errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	

}
