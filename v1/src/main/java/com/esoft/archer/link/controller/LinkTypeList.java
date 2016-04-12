package com.esoft.archer.link.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.language.model.Language;
import com.esoft.archer.link.model.Link;
import com.esoft.archer.link.model.LinkType;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
@Component
@Scope(ScopeType.VIEW)
public class LinkTypeList extends EntityQuery<LinkType>{
	@Logger
	static Log log;
	public LinkTypeList() {
		final String[] RESTRICTIONS = {
				"id like #{linkTypeList.example.id}",
				"name like #{linkTypeList.example.name}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}
}
