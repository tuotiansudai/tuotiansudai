package com.esoft.archer.urlmapping.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.urlmapping.model.UrlMapping;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.REQUEST)
public class UrlMappingList extends EntityQuery<UrlMapping> {
	@Logger
	static Log log;

	public UrlMappingList() {
		final String[] RESTRICTIONS = { "id like #{urlMappingList.example.id}",
				"pattern like #{urlMappingList.example.pattern}",
				"viewId like #{urlMappingList.example.viewId}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
}
