package com.automation.env.pojo;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Environment {

	private List<Env> env = null;

	public List<Env> getEnv() {
		return env;
	}

	public void setEnv(List<Env> env) {
		this.env = env;
	}

}