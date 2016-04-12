package com.esoft.archer.theme.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.SESSION)
public class AdminTheme implements java.io.Serializable {
	
	private final static String ADMIN_CURRENT_THEME_SESSION_KEY = "com.esoft.archer.adminThemeSelected";
	
	private String theme ;
	
	private Map<String, String> themes ;
	
	@PostConstruct
	private void init(){
		themes = new HashMap<String, String>(3);
		themes.put("bluesky", "bluesky");
		themes.put("redmond", "redmond");
		themes.put("aristo", "aristo");
		themes.put("bootstrap", "bootstrap");
	}
	
	public void saveTheme(){
		setCookieValue(theme);
	}
	
	private String getCookieValue(){
		Cookie[] cookies = FacesUtil.getHttpServletRequest().getCookies();
		if(ArrayUtils.isEmpty(cookies)){
			return null ;
		}
		for(Cookie cookie : cookies){
			if(StringUtils.equals(cookie.getName(), ADMIN_CURRENT_THEME_SESSION_KEY)){
				return cookie.getValue();
			}
		}
		return null ;
	}
	
	private void setCookieValue(String value){
		Cookie cookie = new Cookie(ADMIN_CURRENT_THEME_SESSION_KEY, value);
		cookie.setMaxAge(365*24*60*60);
		FacesUtil.getHttpServletResponse().addCookie(cookie);
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTheme() {
		if(theme == null){
			theme = getCookieValue();
		}
		if(theme == null){
			theme = "bootstrap";
		}
		return theme;
	}

	public void setThemes(Map<String, String> themes) {
		this.themes = themes;
	}

	public Map<String, String> getThemes() {
		return themes;
	}
	
	
}
