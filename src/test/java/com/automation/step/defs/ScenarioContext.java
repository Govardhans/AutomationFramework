package com.automation.step.defs;

import java.util.List;
import java.util.Map;

import com.automation.api.pojo.User;
import com.automation.api.request.pojo.CreateUserInput;
import com.automation.api.request.pojo.UpdateUserInput;
import com.automation.api.response.pojo.CreatedUserResponse;
import com.automation.api.response.pojo.GetUsersResponse;
import com.automation.api.response.pojo.UpdatedUserResponse;

public class ScenarioContext {
	public CreatedUserResponse createUserResponse;
	public CreateUserInput createUserInput;
	public List<CreateUserInput> listCreateUserInput;
	public GetUsersResponse getUsersResponse;
	public User matchedUser;
	public UpdateUserInput updateUserInput;
	public UpdatedUserResponse updatedUserResponse;
	public static int testCounter=0;
	public String existingUuid;
	public String existingEmailId;
	public List<CreateUserInput> userDataInputs;
	public Map<String, String> lastUser;
	
}
