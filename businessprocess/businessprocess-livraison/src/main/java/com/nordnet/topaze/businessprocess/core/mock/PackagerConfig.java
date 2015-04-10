package com.nordnet.topaze.businessprocess.core.mock;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class PackagerConfig {

	private String template;

	private Map<String, String> configs = new HashMap<>();

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setConfigs(JSONObject JSONConfig) {
		configs.put("JSONConfig", JSONConfig.toString());
	}

	public String getTemplate() {
		return this.template;
	}

	public String getConfigs() {
		return configs.get("JSONConfig");
	}

}
