package com.automation.step.defs;

import static com.codeborne.selenide.Selenide.open;

import com.automation.object.repository.ErrorPage;
import com.automation.object.repository.UserPage;
import com.automation.scenario.context.ScenarioContext;
import com.codeborne.selenide.Condition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ErrorPageSteps {
	
	private ErrorPage errorPage;
	private UserPage userPage;
	private ScenarioContext scenarioContext;

	public ErrorPageSteps(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
		errorPage = new ErrorPage();
		userPage = new UserPage();
	}

	@When("navigate to {string} page")
	public void navigateToPage(String path) {
		open(path);		
	}

	@Then("error page should displayed msg {string}")
	public void errorPageShouldDisplayed(String msg) {
		errorPage.getErrorMsg().should(Condition.matchesText(msg));
	}

	@Then("verify home button is present on error page")
	public void verifyHomePageIsPresentOnErrorPage() {
		errorPage.getHomeButton().should(Condition.exist);
	}

	
	@When("click on home page button")
	public void clickOnHomePageButton() {
		errorPage.getHomeButton().click();
	}

}
