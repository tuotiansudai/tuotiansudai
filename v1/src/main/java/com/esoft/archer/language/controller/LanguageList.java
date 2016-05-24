package com.esoft.archer.language.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.language.model.Language;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
@Component
@Scope(ScopeType.REQUEST)
public class LanguageList extends EntityQuery<Language>{
	@Logger
	static Log log;
	public LanguageList() {
		final String[] RESTRICTIONS = {
				"id like #{languageList.example.id}",
				"name like #{languageList.example.name}",
				"country = #{languageList.example.country}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}
	
	private List<Language> languages ;
	
	public List<Language> getLanguages(){
		if(languages == null){
			languages = getHt().findByNamedQuery("Language.findEnableLanguage");
		}
		return languages ;
	}
}
