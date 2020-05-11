package com.automation.helper;

import java.util.List;
/**
 * Final utils class wth static methods<br>
 * @author Govardhan
 *
 */
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.automation.api.request.data.QueryPayload;
import com.automation.api.request.data.QueryVariables;
import com.automation.model.User;
import com.automation.scenario.context.Context;
import com.automation.scenario.context.ScenarioContext;
import com.google.gson.Gson;

public final class Utils {

	private Utils() {

	}

	/**
	 * <p>returns random string between a to z </p>
	 * @param lentgh
	 * @return
	 */
	public static String getRandomString(int lentgh) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		
		return new Random().ints(leftLimit, rightLimit + 1).limit(lentgh)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static String convertEmail(String input, ScenarioContext scenarioContext) {
		String output = input;
		if (StringUtils.equals(input, "$random_email")) {
			return Utils.getRandomString(10) + "@xnt.com";
		} else if (StringUtils.equals(input, "$prv_user_email_id")) {
			List<User> inputList = (List<User>) scenarioContext.getContext(Context.USER_INPUT_DATA);
			return inputList.get(0).getEmail();
		} else if (input.equals("$existingEmailId")) {
			return (String.valueOf(scenarioContext.getContext("EXISTING_USER_EMAIL_ID")));
		}
		return output;
	}
	
	/**
	 * @param query
	 * @param variables
	 * @return
	 */
	public static String getBody(String query, QueryVariables variables)  {	

		QueryPayload payload = new QueryPayload();
		payload.setQuery(query);
		payload.setVariables(variables);

		return new Gson().toJson(payload);
	}
}
