package com.esoft.archer.config.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.config.model.ConfigType;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.VIEW)
public class ConfigList extends EntityQuery<Config> implements java.io.Serializable{
	@Logger
	static Log log;

	public ConfigList() {
		final String[] RESTRICTIONS = {
				"id like #{configList.example.id}",
				"name like #{configList.example.name}",
				"configype.id = #{configList.example.configType.id}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}

	@Override
	public void initExample() {
		Config config = new Config();
		config.setConfigType(new ConfigType());
		setExample(config);
	}

	@SuppressWarnings("unchecked")
	public List<Config> getConfigs() {

		getHt().setCacheQueries(true);
		List<Config> configs = getHt().find("from " + Config.class);
		return configs;
	}
	
	public List<Config> getConfigsByType(String type){
		return getHt().find("from Config where configType.id = ? ",type);
	}
}
