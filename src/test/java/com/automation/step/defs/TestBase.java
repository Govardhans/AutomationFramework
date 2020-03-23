package com.automation.step.defs;

import static com.codeborne.selenide.Selenide.closeWebDriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * <h1>Test Base</h1>
 * 
 * @author Govardhan
 * 
 *         <p>
 *         Cucumber hooks are declared in this class
 *         </p>
 *
 */
public class TestBase {

	private static Logger logger = LoggerFactory.getLogger(TestBase.class);
	ApplicationContext context = new AnnotationConfigApplicationContext();
	private ScenarioContext scenarioContext;

	public TestBase(ScenarioContext context) {
		this.scenarioContext = context;
	}

	@Before
	public void init(Scenario scenario) {
		logger.info("\n\n");
		logger.info("---------------------- Test {} Started -----------------------", ++ScenarioContext.testCounter);
	}

	@After
	public void tearDown(Scenario scenario) {
		logger.info("Closing driver");
		closeWebDriver();
		logger.info("-------------------  Test Status :: {} ---------------------", scenario.getStatus());
		logger.info("\n\n");

	}

}