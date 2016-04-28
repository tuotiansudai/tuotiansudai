package com.esoft.archer.theme.controller;


import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Template;
import com.esoft.archer.theme.model.Theme;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.REQUEST)
public class TemplateHome extends EntityHome<Template>{
	
	@Logger static Log log ;
	
	public TemplateHome(){
		setUpdateView( FacesUtil.redirect(ThemeConstants.View.TEMPLATE_LIST) );
	}

	@Override
	protected Template createInstance() {
		Template template = new Template();
		template.setTheme(new Theme());
		return template ;
	}
}
