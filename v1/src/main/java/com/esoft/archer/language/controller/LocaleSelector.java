package com.esoft.archer.language.controller;

import java.util.Locale;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.SESSION)
public class LocaleSelector implements java.io.Serializable{
	
	@Logger
	static Log log ;
	
	public final static String CURRENT_LANGUAGE_SESSION_KEY = "com.esoft.archer.localeSelected";
	
	private Locale locale ;

	public void setLocale(Locale locale) {
		this.locale = locale;
		setSessionValue(locale.getLanguage() + "_" + locale.getCountry());
		setCookieValue(locale.getLanguage() + "_" + locale.getCountry());
	}

	public Locale getLocale() {
		if(locale == null){
			//get locale from cookie
			if(getCookieValue() != null){
				changeLocale(getCookieValue());
			}else{
//				locale = FacesUtil.getCurrentInstance().getViewRoot().getLocale();
				locale = FacesUtil.getExternalContext().getRequestLocale();
			}
		}
		
		return locale;
	}
	
	private String getCookieValue(){
		Cookie[] cookies = FacesUtil.getHttpServletRequest().getCookies();
		if(ArrayUtils.isEmpty(cookies)){
			return null ;
		}
		for(Cookie cookie : cookies){
			if(StringUtils.equals(cookie.getName(), CURRENT_LANGUAGE_SESSION_KEY)){
				return cookie.getValue();
			}
			
		}
		return null ;
	}
	
	private void setSessionValue(String value){
		FacesUtil.setSessionAttribute(CURRENT_LANGUAGE_SESSION_KEY, value);
	}
	
	private void setCookieValue(String value){
		Cookie cookie = new Cookie(CURRENT_LANGUAGE_SESSION_KEY, value);
		cookie.setMaxAge(365*24*60*60);
		FacesUtil.getHttpServletResponse().addCookie(cookie);
	}
	
	public void changeLocale(String locale){
		
		int separator = locale.indexOf("_");
		if(separator > 0 ){
			final String[] info = locale.split("_");
			setLocale(new Locale(info[0],info[1]));
		}
		
	}
	
	public void changeLocale(ValueChangeEvent event){
		changeLocale((String) event.getNewValue());
	}
}
