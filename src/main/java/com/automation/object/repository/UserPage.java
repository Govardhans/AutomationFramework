package com.automation.object.repository;

import static com.codeborne.selenide.Selenide.$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.automation.api.pojo.User;
import com.automation.api.request.pojo.CreateUserInput;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

@Component
public class UserPage extends BasePage implements Page {

	private static final String EMAIL = "Email";
	private SelenideElement createUserButton;
	private SelenideElement userOverviewTable;
	private static Logger logger = LoggerFactory.getLogger(UserPage.class);

	public UserPage() {
		init();
	}

	public void init() {
		createUserButton = $(By.xpath("//button/span[contains(text(),'Create User')]"));
		userOverviewTable = $(By.xpath("//user-table/table"));
	}

	public Map<String, String> getLastUser() {
		ElementsCollection header = userOverviewTable.$$(By.xpath("thead/tr/th"));
		ElementsCollection rows = userOverviewTable.$$(By.xpath("tbody/tr"));
		ElementsCollection rowData = rows.get(rows.size() - 1).$$(By.xpath("td"));

		Map<String, String> userMap = new LinkedHashMap<>();
		for (int i = 0; i < rowData.size() - 1; i++) {
			userMap.put(header.get(i).getText(), rowData.get(i).getText());
		}
		return userMap;
	}

	public List<User> getUsers() {
		ElementsCollection header = userOverviewTable.$$(By.xpath("thead/tr/th"));
		ElementsCollection rows = userOverviewTable.$$(By.xpath("tbody/tr"));

		Map<String, String> userMap;
		List<Map<String, String>> listUsers = new LinkedList<>();
		ElementsCollection rowData;

		for (int i = 0; i < rows.size(); i++) {
			rowData = rows.get(i).$$(By.xpath("td"));
			userMap = new HashMap<>();
			for (int j = 0; j < rowData.size(); j++) {
				userMap.put(header.get(j).getText(), rowData.get(j).getText());
			}
			listUsers.add(userMap);
		}

		listUsers.stream().forEach(user -> logger.info(user.toString()));

		return new ArrayList<>();
	}

	/**
	 * @return the createUserButton
	 */
	public SelenideElement getCreateUserButton() {
		return createUserButton.shouldBe(Condition.exist);
	}

	/**
	 * @return the userOverviewTable
	 */
	public SelenideElement getUserOverviewTable() {
		return userOverviewTable;
	}

	public boolean deleteUser(CreateUserInput input) {
		logger.info("Get users by email ID :: {} ", input.getEmail());
		SelenideElement user = getUserby(EMAIL, input.getEmail());
		
		if (user == null) {
			logger.info("USER not found");
			return false;
		}
		
		int deleteIndex = indexOf(userOverviewTable.$$(By.xpath("thead/tr/th")), "Delete");
		
		if(deleteIndex == -1) {
			logger.info(" \"Delete\" index not found");
			return false;
		}
		user.$(By.xpath("td[" + deleteIndex + "]")).click();

		return true;
	}

	public SelenideElement getUserby(String headerName, String value) {
		ElementsCollection header = userOverviewTable.$$(By.xpath("thead/tr/th"));
		int index = indexOf(header, headerName);		
		ElementsCollection rows = userOverviewTable.$$(By.xpath("tbody/tr/td[" + index + "]"));
		for (int i = rows.size() - 1; i >= 0; i--) {
			if (rows.get(i).getText().equals(value)) {
				return userOverviewTable.$(By.xpath("tbody/tr[" + (i + 1) + "]"));
			}
		}

		return null;
	}

	public int indexOf(ElementsCollection header, String headerName) {
		logger.info("Finding index of {} in user table", headerName);
		for (int i = 0; i < header.size(); i++) {
			if (header.get(i).getText().equalsIgnoreCase(headerName)) {				
				return i + 1;				
			}
		}
		return -1;
	}

}
