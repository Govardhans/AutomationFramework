package com.automation.step.defs;

import static com.codeborne.selenide.Selenide.open;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automation.helper.Utils;
import com.automation.model.User;
import com.automation.object.repository.CreateUserPage;
import com.automation.object.repository.UserPage;
import com.automation.scenario.context.Context;
import com.automation.scenario.context.ScenarioContext;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

/**
 * Login Step's glue code
 * 
 * @author Govardhan
 *
 */
public class UserPageSteps {
	private static Logger logger = LoggerFactory.getLogger(UserPageSteps.class);
//	private ScenarioContext scenarioContext;
	ScenarioContext scenarioContext;
	UserPage userPage = new UserPage();
	CreateUserPage createUserPage = new CreateUserPage();

	public UserPageSteps(ScenarioContext scenarioContext) {
		// this.scenarioContext = scenarioContext;
		this.scenarioContext = scenarioContext;
	}

	@Given("frontend is accessible")
	public void frontendIsAccessible() {
		logger.info("Portal Helth check url is not available");
	}

	@When("navigate to user page")
	public void navigateToUserPage() {
		open("/user");
	}

	@Then("user should landed on USER_PAGE")
	public void userPageShouldBeDisplayed() {
		Assert.assertTrue("User didn't navigat to user page", userPage.getCreateUserButton().exists());
	}

	@Then("click on create user button")
	public void clickOnCreateUserPage() {
		userPage.getCreateUserButton().click();
	}

	@Then("create new user page should be displayed")
	public void createNewUserPageShouldBeDisplayed() {
		Assert.assertTrue("User didn't navigat to create new user page", createUserPage.getFirstName().exists());
	}

	@When("create new user using below details")
	public void createNewUserUsingBelowDetails(DataTable dataTable) throws InterruptedException {
		List<Map<String, String>> userDetails = dataTable.asMaps();
		ObjectMapper mapper = new ObjectMapper();
		List<User> userDataInputs = new ArrayList<>();

		for (Map<String, String> userMap : userDetails) {
			User input = mapper.convertValue(userMap, User.class);
			input.setEmail(getEmail(input.getEmail()));
			// store user data into context
			createUserPage.getFirstName().setValue(input.getFirstName());
			createUserPage.getLastName().setValue(input.getLastName());
			createUserPage.getEmail().setValue(input.getEmail());
			if (input.isNewsletter()) {
				createUserPage.getNewsletterCheckBox().click();
			}

			userDataInputs.add(input);
			createUserPage.getSubmitButton().click();
		}

		scenarioContext.setContext(Context.USER_INPUT_DATA, userDataInputs);
	}

	@Then("Error message {string} should popup")
	public void errorShouldPopup(String errorMsg) {
		logger.info("Verify error message : ");
		createUserPage.getEmailAlreadyExist().should(Condition.text(errorMsg));
	}

	@Then("Error message {string} should display under email address")
	public void errorMessageShouldDisplayUnderEmailAddress(String errorMsg) {
		createUserPage.getEmailError().should(Condition.text(errorMsg));
	}

	@SuppressWarnings("unchecked")
	public String getEmail(String input) {
		String output = input;
		if (StringUtils.equals(input, "$random_email")) {
			return Utils.getRandomString(10) + "@xnt.com";
		} else if (StringUtils.equals(input, "$prv_user_email_id")) {
			List<User> inputList = (List<User>) scenarioContext.getContext(Context.USER_INPUT_DATA);
			return inputList.get(0).getEmail();
		}
		return output;
	}

	@Then("new user should be added in user table at last row. save user details in to {string}")
	public void newUserShouldBeAddedInUserTableAtLastRow(String saveUserDetails) {
		Map<String, String> lastUser = userPage.getLastUser();

		scenarioContext.setContext(saveUserDetails, lastUser);
		logger.info("Added User details are :: {} ", lastUser.toString());
	}

	@SuppressWarnings("unchecked")
//	@Then("verify that user details on user page should be same as details provided in create user step")
	@Then("verify that {string} are same as details provided in create user step")
	public void verifyThatUserDetails(String userDetails) {

		List<User> providedData = (List<User>) scenarioContext.getContext(Context.USER_INPUT_DATA);
		Assert.assertNotNull("User input data is null", providedData);

		Map<String, String> webPageData = (Map<String, String>) scenarioContext.getContext(userDetails);
		Assert.assertNotNull("User data from page is not in context", webPageData);

		List<String> expectedResult = new LinkedList<>();

		expectedResult.add(providedData.get(0).getEmail());
		expectedResult.add(providedData.get(0).getFirstName());
		expectedResult.add(providedData.get(0).getLastName());
		expectedResult.add(providedData.get(0).isNewsletter() ? "done" : "clear");

		// clear done

		List<String> actualResult = new LinkedList<>();
		actualResult.add(webPageData.get("Email"));
		actualResult.add(webPageData.get("First Name"));
		actualResult.add(webPageData.get("Last Name"));
		actualResult.add(webPageData.get("Newsletter"));

		logger.info("Expected user details {} : ", expectedResult);
		logger.info("Actual user details {} : ", actualResult);

		logger.info("comparing user details :  {} ", actualResult.equals(expectedResult));
	}

	@When("delete user by")
	public void deleteUser(DataTable dataTable) {
		List<Map<String, String>> userDetails = dataTable.asMaps();
		ObjectMapper mapper = new ObjectMapper();
		for (Map<String, String> userMap : userDetails) {
			User input = mapper.convertValue(userMap, User.class);
			input.setEmail(getEmail(input.getEmail()));

			logger.info("Delete user :: {} ", input.toString());
			Assert.assertTrue("User not found", userPage.deleteUser(input));
		}
	}

	@Then("user {string} should be removed from user table")
	public void userShouldBeRemovedFromUserTable(String emailId) {
		logger.info("verifying if email address is removed from the user overview table");
		SelenideElement user = userPage.getUserby("Email", getEmail(emailId));
		Assert.assertNull("User is found in table", user);
		logger.info("email address {} didn't find in table", getEmail(emailId));
	}
}
