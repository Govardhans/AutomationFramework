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

import com.automation.api.request.pojo.CreateUserInput;
import com.automation.helper.Utils;
import com.automation.object.repository.CreateUserPage;
import com.automation.object.repository.UserPage;
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
	ScenarioContext state;
	UserPage userPage = new UserPage();
	CreateUserPage createUserPage = new CreateUserPage();

	public UserPageSteps(ScenarioContext state) {
		//this.scenarioContext = scenarioContext;
		this.state = state;
	}

	@Given("frontend is accessible")
	public void frontendIsAccessible() {
		logger.info("Portal Helth check url is not available");
	}

	@When("navigate to user page")
	public void navigateToUserPage() {
		open("/user");
	}

	@Then("user page should be displayed")
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
		List<CreateUserInput> userDataInputs = new ArrayList<>();

		for (Map<String, String> userMap : userDetails) {
			CreateUserInput input = mapper.convertValue(userMap, CreateUserInput.class);
			input.setEmail(getEmail(input.getEmail()));
			// store user data into context
			createUserPage.getFirstName().setValue(input.getFirstName());
			createUserPage.getLastName().setValue(input.getLastName());
			createUserPage.getEmail().setValue(input.getEmail());
			if (input.getNewsletter()) {
				createUserPage.getNewsletterCheckBox().click();
			}

			userDataInputs.add(input);
			createUserPage.getSubmitButton().click();
		}
		state.userDataInputs =  userDataInputs;
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

	public String getEmail(String input) {
		String output = input;
		if (StringUtils.equals(input, "$random_email")) {
			return Utils.getRandomString(10) + "@xnt.com";
		} else if (StringUtils.equals(input, "$prv_user_email_id")) {
			List<CreateUserInput> inputList = state.userDataInputs;
			return inputList.get(0).getEmail();
		}
		return output;
	}

	@Then("new user should be added in user table at last row")
	public void newUserShouldBeAddedInUserTableAtLastRow() {
		Map<String, String> lastUser = userPage.getLastUser();
		state.lastUser = lastUser;
		logger.info("Added User details are :: {} ", lastUser.toString());
	}

	@SuppressWarnings("unchecked")
	@Then("verify that user details on user page should be same as details provided in create user step")
	public void verifyThatUserDetails() {
		List<CreateUserInput> providedData = state.userDataInputs;
		providedData.get(0);

		Map<String, String> webPageData = state.lastUser;

		List<String> expectedResult = new LinkedList<>();
		expectedResult.add(providedData.get(0).getEmail());
		expectedResult.add(providedData.get(0).getFirstName());
		expectedResult.add(providedData.get(0).getLastName());
		expectedResult.add(providedData.get(0).getNewsletter() ? "done" : "clear");

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
			CreateUserInput input = mapper.convertValue(userMap, CreateUserInput.class);
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
