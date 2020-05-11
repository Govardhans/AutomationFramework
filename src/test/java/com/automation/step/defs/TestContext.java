package com.automation.step.defs;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
	Map<String, Object> context = new HashMap<>();
	
	
	/**
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	
}
