package com.automation.step.defs;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automation.api.request.data.DeleteUserQueryVariable;
import com.automation.api.request.data.UserQueryVariable;
import com.automation.api.response.data.GetUsersResponse;
import com.automation.api.response.data.UserResponse;
import com.automation.api.response.data.UserResponse.CreatedUserData.ResponseUser;
import com.automation.helper.Utils;
import com.automation.model.User;
import com.automation.scenario.context.Context;
import com.automation.scenario.context.ScenarioContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import ch.qos.logback.classic.pattern.Util;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

/**
 * 
 * @author Govi
 *
 */
public class BackendSteps {

	private static final String GET_USERS_QUERY = "{\"query\":\"{users { uuid lastName firstName email newsletter createdAt lastModifiedAt}}\",\"variables\":null,\"operationName\":null}";
	private static final String CREATE_USERS_QUERY = "mutation ($input : CreateUserInput!) { createUser(input: $input){ ok error user{ uuid  } } }";
	private static Logger logger = LoggerFactory.getLogger(BackendSteps.class);

	private ScenarioContext scenarioContext;

	public BackendSteps(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@Given("api is accessible")
	public void apiIsAccessible() {
		logger.info("End point :: {}{}", RestAssured.baseURI, RestAssured.basePath);

		Response response = null;
		try {
			response = given().header("Content-Type", "application/json").body(GET_USERS_QUERY).post().then().extract()
					.response();
		} catch (Exception ex) {
			logger.error("End point is not accessible.. Error msg is => " + ex.getMessage());
			Assert.fail("End point is not accessible");
		}

		logger.info("Response code is :: {} ", response.getStatusCode());

		Assert.assertEquals(200, response.getStatusCode());
	}

	@When("create new user using query {string}")
	public void createNewUserUsingQuery(String query, DataTable dataTable) {
		logger.info("create new user");
		Response response = postRequest(query, dataTable, Context.USER_INPUT_DATA).extract().response();
		logger.info("response status :: {}", response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());

//		de-serialize json into UserResponse obj
		UserResponse createUserResponse = new Gson().fromJson(response.getBody().asString(), UserResponse.class);

		this.scenarioContext.setContext(Context.CREATED_USER_RESPONSE, createUserResponse);
		logger.info("response body :: {}", response.getBody().asString());
	}

	@Then("updated user response body should have")
	public void updatedUserResponseBodyShouldHave(DataTable dataTable) {
		validateResponseBody(Context.UPDATED_USER_RESPONSE, dataTable);
	}

	@Then("created user response body should have")
	public void responseBodyShouldHave(DataTable dataTable) {
		validateResponseBody(Context.CREATED_USER_RESPONSE, dataTable);
	}

	/**
	 * 
	 * @param updatedUserResponse
	 * @param dataTable
	 */
	private void validateResponseBody(Context updatedUserResponse, DataTable dataTable) {
		UserResponse userResponse = (UserResponse) this.scenarioContext.getContext(updatedUserResponse);

		ResponseUser actualResult = userResponse.getData().getResponseUser();
		ResponseUser expectedResult = new Gson().fromJson(dataTable.asMaps().get(0).toString(), ResponseUser.class);

		Assert.assertEquals(expectedResult, actualResult);
	}

	@Then("created user's details in response body should match")
	public void createdUserSDetailsInResponseBodyShouldMatch(DataTable dataTable) {
		User expectedResult = new Gson().fromJson(dataTable.asMaps().get(0).toString(), User.class);
		UserResponse userResponse = (UserResponse) this.scenarioContext.getContext(Context.CREATED_USER_RESPONSE);
		User actualResult = userResponse.getData().getResponseUser().getUser();

		Assert.assertEquals(expectedResult, actualResult);
	}

	@When("retrieved user's details in response body should match")
	public void retrievedUserSDetailsInResponseBodyShouldMatch(DataTable dataTable) {
		User expectedResult = new Gson().fromJson(dataTable.asMaps().get(0).toString(), User.class);
		User actualResult = (User) this.scenarioContext.getContext(Context.RETRIEVE_USER);

		Assert.assertEquals(expectedResult, actualResult);
	}

	@Then("updated user's details in response body should match")
	public void updatedUserSDetailsInResponseBodyShouldMatch(DataTable dataTable) {
		User expectedResult = new Gson().fromJson(dataTable.asMaps().get(0).toString(), User.class);
		UserResponse userResponse = (UserResponse) this.scenarioContext.getContext(Context.UPDATED_USER_RESPONSE);
		User actualResult = userResponse.getData().getResponseUser().getUser();

		Assert.assertEquals(expectedResult, actualResult);
	}

	@When("retrieve user using query {string} and email {string}")
	public void retrieveUserUsingQueryAndEmail(String query, String emailId) {

		emailId = Utils.convertEmail(emailId, scenarioContext);
		logger.info("Retrieving users using email ID :: {}", emailId);
		String body = Utils.getBody(query, null);

		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();
		GetUsersResponse usersResponse = new Gson().fromJson(response.getBody().asString(), GetUsersResponse.class);
		this.scenarioContext.setContext("GET_USER_RESPONSE", usersResponse);

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
			this.scenarioContext.setContext(Context.RETRIEVE_USER, matchedUser);
		}
	}

	@When("retrieve all users using query {string}")
	public void retrieveUserUsingQuery(String query) {
		List<User> users = getUsersFromApiResponse(query);
		this.scenarioContext.setContext(Context.USERS, users);
	}

	@SuppressWarnings("unchecked")
	@Then("user count shoud be {int}")
	public void userCountShoudBe(Integer int1) {
		List<User> users = (List<User>) this.scenarioContext.getContext(Context.USERS);
		Assert.assertEquals(0, users.size());
	}

	@SuppressWarnings("unchecked")
	@And("delete all users using query {string}")
	public void deleteAllUsers(String query) {
		List<User> users = (List<User>) this.scenarioContext.getContext(Context.USERS);
		for (User user : users) {
			Response response = deleteUser(query, user);
			Assert.assertEquals(200, response.statusCode());
			logger.info("{} is delete... ", user.getUuid());
		}
	}

	@When("delete user using query {string}")
	public void deleteUserUsingQuery(String query, DataTable dataTable) {
		logger.info("Delete user");
		User user = new Gson().fromJson(dataTable.asMaps().get(0).toString(), User.class);
		Response response = deleteUser(query, user);
		Assert.assertEquals(200, response.statusCode());
		
		UserResponse deleteUserResponse = new Gson().fromJson(response.getBody().asString(), UserResponse.class);
		
		scenarioContext.setContext(Context.DELETE_USER_RESPONSE, deleteUserResponse);		 
	}

	/**
	 * <p>
	 * delete user
	 * </p>
	 * 
	 * @param query
	 * @param user
	 */
	private Response deleteUser(String query, User user) {
		DeleteUserQueryVariable variables = new DeleteUserQueryVariable();
		variables.setUuid(user.getUuid());
		String body = Utils.getBody(query, variables);
		return given().header("Content-Type", "application/json").body(body).post().then().extract().response();
	}

	/**
	 * <p>
	 * this method retrieves all users and then check the created_user in response
	 * 
	 * @param query
	 */
	@When("retrieve created user using query {string}")
	public void retrieveCreatedUserUsingQuery(String query) {
		logger.info("Retrieving users");
		List<User> users = getUsersFromApiResponse(query);
		// get Uuid of created user
		UserResponse alteredUserResponse = (UserResponse) this.scenarioContext
				.getContext(Context.CREATED_USER_RESPONSE);
		User createdUser = alteredUserResponse.getData().getResponseUser().getUser();
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
			this.scenarioContext.setContext(Context.RETRIEVE_USER, matchedUser);
		}

	}

	/**
	 * <p>
	 * Get users from api response
	 * </p>
	 * 
	 * @param query
	 * @return
	 */
	private List<User> getUsersFromApiResponse(String query) {
		String body = Utils.getBody(query, null);
		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();

		// fetch all users using api
		GetUsersResponse usersResponse = new Gson().fromJson(response.getBody().asString(), GetUsersResponse.class);
		this.scenarioContext.setContext("GET_USER_RESPONSE", usersResponse);
		List<User> users = usersResponse.getData().getUsers();
		return users;
	}

	@When("create another user using newly created users details")
	public void createAnotherUserUsingNewlyCreatedUsersDetails() {
		logger.info("create new user");

		User input = (User) this.scenarioContext.getContext(Context.USER_INPUT_DATA);
		UserQueryVariable variables = new UserQueryVariable();
		variables.setInput(input);
		String body = Utils.getBody(CREATE_USERS_QUERY, variables);
		logger.info("payload :: {} ", body);

		Response response = given().header("Content-Type", "application/json").body(body).post().then().extract()
				.response();
		logger.info("response status :: {}", response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());

		UserResponse createUserResponse = new Gson().fromJson(response.getBody().asString(), UserResponse.class);
		this.scenarioContext.setContext(Context.CREATED_USER_RESPONSE, createUserResponse);

		logger.info("response body :: {}", response.getBody().asString());
	}

	@When("update user using query {string}")
	public void updateUserUsingQuery(String query, DataTable dataTable) {
		logger.info("Update user");

		Response response = postRequest(query, dataTable, Context.UPDATED_USER_DATA).extract().response();
		logger.info("response status :: {}", response.getStatusCode());
		Assert.assertEquals(200, response.getStatusCode());

		UserResponse updatedUserResponse = new Gson().fromJson(response.getBody().asString(), UserResponse.class);
		this.scenarioContext.setContext(Context.UPDATED_USER_RESPONSE, updatedUserResponse);

		logger.info("response body :: {}", response.getBody().asString());
	}

	/**
	 * <p>
	 * <code>postRequest</code> returns restAssured validatableResponse
	 * </p>
	 * 
	 * @param query
	 * @param dataTable
	 * @param storeUserInput
	 * @return
	 */
	private ValidatableResponse postRequest(String query, DataTable dataTable, Context storeUserInput) {
		List<Map<String, String>> userData = dataTable.asMaps();
		ObjectMapper mapper = new ObjectMapper();
		User input = mapper.convertValue(userData.get(0), User.class);

		logger.info("");
		input.setEmail(Utils.convertEmail(input.getEmail(), scenarioContext));

		if (StringUtils.equals(input.getUuid(), ("$createdUserUuid"))) {
			UserResponse alteredUserResponse = (UserResponse) this.scenarioContext
					.getContext(Context.CREATED_USER_RESPONSE);
			input.setUuid(alteredUserResponse.getData().getResponseUser().getUser().getUuid());
		}

		this.scenarioContext.setContext(storeUserInput, input);
		UserQueryVariable variables = new UserQueryVariable();
		variables.setInput(input);
		String body = Utils.getBody(query, variables);
		logger.info("payload :: {} ", body);

		return given().header("Content-Type", "application/json").body(body).post().then();
	}

	@Given("system must have atleast one user")
	public void systemHasMultipleUsers() {
		Response response = given().header("Content-Type", "application/json").body(GET_USERS_QUERY).post().then()
				.extract().response();

		GetUsersResponse usersResponse = new Gson().fromJson(response.getBody().asString(), GetUsersResponse.class);
		logger.info("Response code is :: {} ", response.getStatusCode());
		int usersSize = usersResponse.getData().getUsers().size();
		logger.info("Users count :: {}", usersSize);
		Assert.assertTrue("minimum 1 users needs to be system to test this", usersSize >= 1);

		// store existing uuid
		this.scenarioContext.setContext("EXISTING_USER_EMAIL_ID", usersResponse.getData().getUsers().get(0).getEmail());

	}

	@SuppressWarnings("unchecked")
	@Then("verify frontend and backend user details")
	public void compareFeAndBeUserDetails() {

		List<User> feUserDataList = (List<User>) this.scenarioContext.getContext(Context.USER_INPUT_DATA);
		User feUserData = feUserDataList.get(0);
		User beUserData = (User) this.scenarioContext.getContext(Context.RETRIEVE_USER);

		logger.info("--------------------------------");
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.getFirstName(), feUserData.getFirstName());
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.getLastName(), feUserData.getLastName());
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.getEmail(), feUserData.getEmail());
		logger.info("[first name] :: BE [{}] FE [{}]", beUserData.isNewsletter(), feUserData.isNewsletter());
		logger.info("--------------------------------");

		Assert.assertEquals(beUserData.getFirstName(), feUserData.getFirstName());
		Assert.assertEquals(beUserData.getLastName(), feUserData.getLastName());
		Assert.assertEquals(beUserData.getEmail(), feUserData.getEmail());
		Assert.assertEquals(beUserData.isNewsletter(), feUserData.isNewsletter());
	}

	@When("create user and validate schema {string} using query {string}")
	public void createUserAndValidateSchemaUsingQuery(String schemaFile, String query, DataTable dataTable) {
		logger.info("create new user");
		SchemaValidation(schemaFile, query, dataTable, Context.USER_INPUT_DATA);
	}

	@Then("update user and validate schema {string} using query {string}")
	public void updateUserAndValidateSchemaUsingQuery(String schemaFile, String query, DataTable dataTable) {
		logger.info("Update user");
		SchemaValidation(schemaFile, query, dataTable, Context.UPDATED_USER_DATA);
	}

	/**
	 * <p>
	 * Schema Validation
	 * </p>
	 * 
	 * @param schemaFile
	 * @param query
	 * @param dataTable
	 * @param saveInputContext
	 */
	private void SchemaValidation(String schemaFile, String query, DataTable dataTable, Context saveInputContext) {

		ValidatableResponse then = postRequest(query, dataTable, saveInputContext);

		logger.info("Response body :");
		then.extract().response().getBody().prettyPrint();

		logger.info("Validating Schema");
		then.assertThat().body(matchesJsonSchemaInClasspath(schemaFile));
		logger.info("Schema Validation is successful !!");
	}

	@Then("delete user response body should have")
	public void deleteUserResponseBodyShouldHave(DataTable dataTable) {
		validateResponseBody(Context.DELETE_USER_RESPONSE, dataTable);
	}

}
