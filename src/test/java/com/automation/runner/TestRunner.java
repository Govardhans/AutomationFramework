package com.automation.runner;

import java.io.File;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

import com.automation.env.pojo.Env;
import com.automation.env.pojo.Environment;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit.TextReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "classpath:features", 
		glue = "com.automation.step.defs",
		tags = {"not @skip"},
		dryRun = false,
		monochrome = true,
		snippets = SnippetType.CAMELCASE,
		plugin = {
					"json:target/json-cucumber-reports/cukejson.json", 
					"html:target/cucumber-report/1.html", "pretty", 
					"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"					
				 })

/**
 *  
 * @author Govardhan
 *
 */
public class TestRunner {

	public static Logger logger = LoggerFactory.getLogger(TestRunner.class);	
	
	/**
	 * Selenide rule for taking screenshots
	 */
	@Rule
	public TestRule report = new TextReport().onFailedTest(true).onSucceededTest(true);

	
	@BeforeClass
	public static void setUp() {
					
		SpringApplication.run(TestRunner.class, new String[] {});		
		String testEnv = System.getProperty("env", "qa"); 
		String browser = System.getProperty("browser", "");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		try {
			Environment envDetails = mapper.readValue(new File("config/env.yaml"), Environment.class);
			Optional<Env> env = envDetails.getEnv().stream().filter((e) -> e.getName().equalsIgnoreCase(testEnv))
					.findFirst();

			if (env.isPresent()) {
				
				Configuration.baseUrl = env.get().getWebDetails().getUrl();
				Configuration.browser = System.getProperty("browser", "chrome");
				Configuration.fastSetValue = true;
				Configuration.timeout = 10000;
				
				RestAssured.baseURI = env.get().getApiDetails().getUri();
				RestAssured.basePath = "/graphql";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown() {
		logger.info("Tear Down");
	}
}
