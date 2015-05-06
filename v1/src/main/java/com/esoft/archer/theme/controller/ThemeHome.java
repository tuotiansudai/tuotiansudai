package com.esoft.archer.theme.controller;


import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Theme;
import com.esoft.archer.theme.service.ThemeService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.REQUEST)
public class ThemeHome extends EntityHome<Theme>{
	
	@Logger static Log log ;
	@Resource
	private ThemeService themeService ;
	
	private static StringManager sm = StringManager.getManager(ThemeConstants.Package);
	
	public ThemeHome(){
		setUpdateView( FacesUtil.redirect(ThemeConstants.View.THEME_LIST) );
	}
	
	public String setDefaultTheme(final String themeId){
		
		boolean success = themeService.setDefaultTheme(themeId);
		if(success){
			final String message = sm.getString("log.setDefaultTheme",themeId);
			if(log.isInfoEnabled()){
				log.info(message);
			}
			FacesUtil.addInfoMessage(message);
			//FIXME:
			FacesUtil.setSessionAttribute(ThemeConstants.SESSION_KEY_PRETTY_CONFIG, null);
			FacesUtil.setSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME, themeId);
		}
		
		return FacesUtil.redirect(ThemeConstants.View.THEME_LIST);
	}
	
}
