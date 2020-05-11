package com.automation.scenario.context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {	

	private Map<String, Object> scenarioContextMap;

	public ScenarioContext() {
		scenarioContextMap = new HashMap<>();
	}

	public void setContext(Context key, Object value) {
		scenarioContextMap.put(key.toString(), value);
	}

	public Object getContext(Context key) {				
		return scenarioContextMap.get(key.toString());
	}

	public Boolean isContains(Context key) {
		return scenarioContextMap.containsKey(key.toString());
	}

	public void setContext(String key, Object value) {
		scenarioContextMap.put(key, value);
	}

	public Object getContext(String key) {
		return scenarioContextMap.get(key);
	}

	public Boolean isContains(String key) {
		return scenarioContextMap.containsKey(key);
	}

}