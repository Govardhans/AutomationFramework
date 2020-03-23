package com.automation.api.request.pojo;

import com.google.gson.Gson;

public class Runner {

	public static void main(String[] args) {
		Payload payload = new Payload();
		payload.setQuery("mutation ($input  : CreateUserInput!) {  createUser(input: $input){      ok    error    user{      firstName    }  }}");
		
		CreateUserData var = new CreateUserData();
		
		CreateUserInput input = new CreateUserInput();
		input.setEmail("govardhan.sanap2@gmail.com");
		input.setFirstName("Govardhan");
		input.setLastName("Sanap");
		input.setNewsletter(true);
		var.setInput(input);
		
		payload.setVariables(var);
		
		Gson gson = new Gson();
		String json = gson.toJson(payload);
		System.out.println(json);
		
		payload.setQuery("mutation ($input  : UpdateUserInput!) {  updateUser(input: $input){    ok    error    user{      firstName    }  }}");
		
		UpdateUserData data = new UpdateUserData();
		UpdateUserInput input2 = new UpdateUserInput();
		
		input2.setEmail("govardhan.sanap2@gmail.com");
		input2.setFirstName("Govardhan");
		input2.setLastName("Sanap");
		input2.setNewsletter(true);
		input2.setUuid("");
		data.setInput(input2);
		
		payload.setVariables(data);
		json = gson.toJson(payload);
		System.out.println(json);
		
		DeleteUserData deleteUser = new DeleteUserData();
		deleteUser.setUuid("");
		payload.setQuery("mutation ($uuid  : String!) {deleteUser(uuid: $uuid){   ok    }}");
		payload.setVariables(deleteUser);
		
		json = gson.toJson(payload);
		System.out.println(json);
		
		
		
	}

}

