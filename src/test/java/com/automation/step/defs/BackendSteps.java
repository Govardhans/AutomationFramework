package com.automation.step.defs;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automation.api.pojo.User;
import com.automation.api.request.pojo.CreateUserData;
import com.automation.api.request.pojo.CreateUserInput;
import com.automation.api.request.pojo.Payload;
import com.automation.api.request.pojo.UpdateUserData;
import com.automation.api.request.pojo.UpdateUserInput;
import com.automation.api.response.pojo.CreatedUserResponse;
import com.automation.api.response.pojo.GetUsersResponse;
import com.automation.api.response.pojo.ResponseUser;
import com.automation.api.response.pojo.UpdatedUserResponse;
import com.automation.helper.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BackendSteps {

	private static final String GET_USERS_QUERY = "{\"query\":\"{users { uuid lastName firstName email newsletter createdAt lastModifiedAt}}\",\"variables\":null,\"operationName\":null}";
	private static final String CREATE_USERS_QUERY = "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ firstName } }}";
	private static Logger logger = LoggerFactory.getLogger(BackendSteps.class);
	private ScenarioContext state;

	public BackendSteps(ScenarioContext state) {
		this.state = state;
	}

	@Given("api is accessible")
	public void apiIsAccessible() {
		logger.info("End point :: {}{}", RestAssured.baseURI, RestAssured.basePath);

		Response response = given().header("Content-Type", "application/json").body(GET_USERS_QUERY).post().then()
				.extract().response();

		logger.info("Response code is :: {} ", response.getStatusCode());

		Assert.assertEquals(200, response.getStatusCode());
	}

	@When("create new user using query {string}")
	public void createNewUserUsingQuery(String query, DataTable dataTable) {
		logger.info("create new user");

		List<Map<String, String>> userData = dataTable.asMaps();

		ObjectMapper mapper = new ObjectMapper();
		CreateUserInput input = mapper.convertValue(userData.get(0), CreateUserInput.class);

		if (StringUtils.equalsIgnoreCase(input.getEmail(), "$random_email")) {
			input.setEmail(Utils.getRandomString(10) + "@xcnt.com");
		}
		;

		state.createUserInput = input;

		Payload payload = new Payload();
		CreateUserData variables = new CreateUserData();
		variables.setInput(input);

		payload.setQuery(query);
		payload.setVariables(variables);

		Gson gson = new Gson();
		String body = gson.toJson(payload);

		logger.info("payload :: {} ", body);

		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();

		logger.info("response status :: {}", response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());

		CreatedUserResponse createUserResponse = gson.fromJson(response.getBody().asString(),
				CreatedUserResponse.class);
		state.createUserResponse = createUserResponse;

		logger.info("response body :: {}", response.getBody().asString());
	}

	@Then("updated user response body should have")
	public void updatedUserResponseBodyShouldHave(DataTable dataTable) {
		ResponseUser actualResult = (ResponseUser) this.state.updatedUserResponse.getData().getResponseUser();
		Gson gson = new Gson();
		List<Map<String, String>> userData = dataTable.asMaps();
		ResponseUser expectedResult = gson.fromJson(userData.get(0).toString(), ResponseUser.class);

		compareResult(actualResult, expectedResult);
	}

	@Then("created user response body should have")
	public void responseBodyShouldHave(DataTable dataTable) {
		ResponseUser actualResult = (ResponseUser) this.state.createUserResponse.getData().getResponseUser();
		Gson gson = new Gson();
		List<Map<String, String>> userData = dataTable.asMaps();
		ResponseUser expectedResult = gson.fromJson(userData.get(0).toString(), ResponseUser.class);

		compareResult(actualResult, expectedResult);
	}

	private void compareResult(ResponseUser actualResult, ResponseUser expectedResult) {
		logger.info("verify response body");
		logger.info("----------------------------validation------------------------------");
		logger.info("Error [actual] = {} :: [expected] = {} ", actualResult.getError(), expectedResult.getError());

		Assert.assertEquals(expectedResult.getError(), actualResult.getError());

		logger.info("OK [actual] = {} :: [expected] = {} ", actualResult.getOk(), expectedResult.getOk());
		Assert.assertEquals(expectedResult.getOk(), actualResult.getOk());
		logger.info("---------------------------------------------------------------------");
	}

	@When("retrieve user using query {string} and email {string}")
	public void retrieveUserUsingQueryAndEmail(String query, String emailId) {
		
		emailId = convertEmail(emailId);
		logger.info("Retrieving users using email ID :: {}", emailId);
		GetUsersResponse usersResponse = getUsersPayload(query);
		// find new user in list
		
		List<User> users = usersResponse.getData().getUsers();
		User matchedUser = null;

		for (int i = users.size() - 1; i >= 0; i--) {
			if (StringUtils.equals(users.get(i).getEmail(), emailId)) {
				matchedUser = users.get(i);
				logger.info("User found : {} ", users.get(i).toString());
				break;
			}
		}

		Assert.assertNotNull("Couldn't find user", matchedUser);
		
		if (matchedUser != null) {
			state.matchedUser = matchedUser;
		}
	}	
	
	public String convertEmail(String input) {
		String output = input;
		if (StringUtils.equals(input, "$random_email")) {
			return Utils.getRandomString(10) + "@xnt.com";
		} else if (StringUtils.equals(input, "$prv_user_email_id")) {
			List<CreateUserInput> inputList = state.userDataInputs;
			return inputList.get(0).getEmail();
		}
		return output;
	}

	@When("retrieve user using query {string}")
	public void retrieveUserUsingQuery(String query) {
		logger.info("Retrieving users");
		GetUsersResponse usersResponse = getUsersPayload(query);

		// find new user in list

		List<User> users = usersResponse.getData().getUsers();

		User createdUser = state.createUserResponse.getData().getResponseUser().getUser();
		User matchedUser = null;

		if (createdUser.getUuid() != null) {

			for (int i = users.size() - 1; i >= 0; i--) {
				if (StringUtils.equals(users.get(i).getUuid(), createdUser.getUuid())) {
					matchedUser = users.get(i);
					logger.info("User found : {} ", users.get(i).toString());
					break;
				}
			}
		} else if (createdUser.getEmail() != null) {
			for (int i = users.size() - 1; i >= 0; i--) {
				if (StringUtils.equals(users.get(i).getEmail(), createdUser.getEmail())) {
					matchedUser = users.get(i);
					logger.info("User found : {} ", users.get(i).toString());
					break;
				}
			}
		} else {
			logger.error("Couldn't find user in USERS response");
		}

		if (matchedUser != null) {
			state.matchedUser = matchedUser;
		}

	}

	private GetUsersResponse getUsersPayload(String query) {
		Payload payload = new Payload();
		payload.setQuery(query);
		payload.setVariables(null);
		Gson gson = new Gson();
		String body = gson.toJson(payload);
		logger.info("payload :: {} ", body);
		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();
		GetUsersResponse usersResponse = gson.fromJson(response.getBody().print(), GetUsersResponse.class);
		state.getUsersResponse = usersResponse;
		return usersResponse;
	}

	@When("create another user using newly created users details")
	public void createAnotherUserUsingNewlyCreatedUsersDetails() {
		logger.info("create new user");

		CreateUserInput input = state.createUserInput;
		Payload payload = new Payload();
		CreateUserData variables = new CreateUserData();
		variables.setInput(input);
		payload.setQuery(CREATE_USERS_QUERY);
		payload.setVariables(variables);

		Gson gson = new Gson();
		String body = gson.toJson(payload);

		logger.info("payload :: {} ", body);

		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();

		logger.info("response status :: {}", response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());

		CreatedUserResponse createUserResponse = gson.fromJson(response.getBody().print(), CreatedUserResponse.class);
		state.createUserResponse = createUserResponse;

		logger.info("response body :: {}", response.getBody().asString());
	}

	@When("update user using query {string}")
	public void updateUserUsingQuery(String query, DataTable dataTable) {
		logger.info("Update user");

		List<Map<String, String>> userData = dataTable.asMaps();

		ObjectMapper mapper = new ObjectMapper();
		UpdateUserInput input = mapper.convertValue(userData.get(0), UpdateUserInput.class);

		if (input.getEmail().equals("$existingEmailId")) {
			input.setEmail(state.existingEmailId);
		}

		if (input.getUuid().equals("$createdUserUuid")) {
			input.setUuid(state.createUserResponse.getData().getResponseUser().getUser().getUuid());
		}

		state.updateUserInput = input;

		Payload payload = new Payload();
		UpdateUserData variables = new UpdateUserData();
		variables.setInput(input);

		payload.setQuery(query);
		payload.setVariables(variables);

		Gson gson = new Gson();
		String body = gson.toJson(payload);

		logger.info("payload :: {} ", body);

		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();

		logger.info("response status :: {}", response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());

		UpdatedUserResponse updatedUserResponse = gson.fromJson(response.getBody().asString(),
				UpdatedUserResponse.class);
		state.updatedUserResponse = updatedUserResponse;

		logger.info("response body :: {}", response.getBody().asString());
	}

	@Given("system must have atleast one user")
	public void systemHasMultipleUsers() {
		Response response = given().header("Content-Type", "application/json").body(GET_USERS_QUERY).post().then()
				.extract().response();

		Gson gson = new Gson();
		GetUsersResponse usersResponse = gson.fromJson(response.getBody().print(), GetUsersResponse.class);
		logger.info("Response code is :: {} ", response.getStatusCode());

		int usersSize = usersResponse.getData().getUsers().size();

		logger.info("Users count :: {}", usersSize);
		Assert.assertTrue("minimum 1 users needs to be system to test this", usersSize >= 1);

		// store existing uuid
		state.existingEmailId = usersResponse.getData().getUsers().get(0).getEmail();

	}
	
	@Then("verify frontend and backend user details")
	public void compareFeAndBeUserDetails() {
		List<CreateUserInput> userDataInputs = state.userDataInputs;
		Assert.assertNotNull(userDataInputs);
		
		CreateUserInput feUserData = userDataInputs.get(0);
		
		User beUserData = state.matchedUser;
		
		logger.info("--------------------------------");
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.getFirstName(), feUserData.getFirstName());
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.getLastName(), feUserData.getLastName());
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.getEmail(), feUserData.getEmail());
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.isNewsletter(), feUserData.getNewsletter());
		logger.info("--------------------------------");
		
		Assert.assertEquals(beUserData.getFirstName(), feUserData.getFirstName());
		Assert.assertEquals(beUserData.getLastName(), feUserData.getLastName());
		Assert.assertEquals(beUserData.getEmail(), feUserData.getEmail());
		Assert.assertEquals(beUserData.isNewsletter(), feUserData.getNewsletter());
	}
}
