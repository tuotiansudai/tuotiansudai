package com.esoft.archer.theme.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.REQUEST)
public class ThemeRenders {
	
	
	private Map<String, String> page ;
	
	private final static String EL_PATTERN = "[#{](.*?)[}]";
	private final static String TOP_PATH = FacesUtil.getAppRealPath() + "/site/components/search.xhtml";
	
	public ThemeRenders(){
		page = new HashMap<String, String>();
		StringBuffer render = new StringBuffer("");
		try {
			render.append(FileUtils.readFileToString(new File(TOP_PATH))) ;
			Pattern p = Pattern.compile(EL_PATTERN);
			Matcher m = p.matcher(render);
			while (m.find()) {
				final String el = m.group();
				Object elValue = FacesUtil.getExpressionValue(el);
				
				if (elValue != null && !elValue.toString().equals("")) {
					m.appendReplacement(render, elValue.toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		page.put("top", render.toString());
//		page.put("top", "/site/components/search.xhtml");
	}
	
	public void setPage(Map<String, String> page) {
		this.page = page;
	}

	public Map<String, String> getPage() {
		return page;
	}
}
