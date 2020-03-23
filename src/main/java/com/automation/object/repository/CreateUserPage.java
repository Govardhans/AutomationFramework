package com.automation.object.repository;

import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class CreateUserPage extends BasePage implements Page {

	private static final String ERROR_MESSAGE = "parent::div/parent::div/following-sibling::div[2]//mat-error";
	private SelenideElement firstName;
	private SelenideElement lastName;
	private SelenideElement submitButton;
	private SelenideElement email;
	private SelenideElement newsletterCheckBox;
	private SelenideElement emailAlreadyExist;
	
	

	public CreateUserPage() {
		init();
	}

	@Override
	public void init() {
		firstName = $(By.xpath("//input[@formcontrolname='firstName']"));
		lastName = $(By.xpath("//input[@formcontrolname='lastName']"));
		email = $(By.xpath("//input[@formcontrolname='email']"));
		newsletterCheckBox = $(By.xpath("//mat-checkbox[@formcontrolname='newsletter']"));
		submitButton = $(By.xpath("//button[@type='submit']"));
		emailAlreadyExist = $(By.xpath("//div[@class='cdk-live-announcer-element cdk-visually-hidden']"));
	}

	public CreateUserPage firstName(String firstName) {
		this.firstName.setValue(firstName);
		return this;
	}

	public CreateUserPage lastName(String lastName) {
		this.lastName.setValue(lastName);
		return this;
	}

	public CreateUserPage email(String email) {
		this.email.setValue(email);
		return this;
	}

	public CreateUserPage newsletter(boolean trueOrFalse) {
		if (trueOrFalse) {
			this.newsletterCheckBox.click();
		}
		return this;
	}

	public void submit() {
		this.submitButton.click();
	}

	
	public SelenideElement getFirstNameError() {
		return firstName.$(By.xpath(ERROR_MESSAGE));		
	}

	
	public SelenideElement getLastNameError() {
		return lastName.$(By.xpath(ERROR_MESSAGE));
	}
	
	public SelenideElement getEmailError() {
		return email.$(By.xpath(ERROR_MESSAGE));
	}


	/**
	 * @return the emailAlreadyExist
	 */
	public SelenideElement getEmailAlreadyExist() {
		return emailAlreadyExist;
	}

	/**
	 * @return the firstName
	 */
	public SelenideElement getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public SelenideElement getLastName() {
		return lastName;
	}

	/**
	 * @return the submitButton
	 */
	public SelenideElement getSubmitButton() {
		return submitButton;
	}

	/**
	 * @return the email
	 */
	public SelenideElement getEmail() {
		return email;
	}

	/**
	 * @return the newsletterCheckBox
	 */
	public SelenideElement getNewsletterCheckBox() {
		return newsletterCheckBox;
	}
	
	

}
