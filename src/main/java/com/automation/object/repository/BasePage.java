package com.automation.object.repository;

import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class BasePage implements Page{

	private SelenideElement toolbarImage;

	public BasePage() {
		init();
	}

	public void init() {
		toolbarImage = $(By.xpath("//mat-toolbar/a"));
	}

}
