package com.esoft.archer.theme.controller;


import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.menu.MenuConstants;
import com.esoft.archer.menu.model.MenuType;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.ComponentParameter;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.REQUEST)
public class ComponentParameterHome extends EntityHome<ComponentParameter>{
	
	@Logger static Log log ;
	
	public ComponentParameterHome(){
//		setUpdateView( FacesUtil.redirect(ThemeConstants.View.COMPONENT_EDIT+"?id="+getId()) );
	}
	
	@Override
	public String getUpdateView() {
		
		return FacesUtil.redirect(ThemeConstants.View.COMPONENT_EDIT) +"&id="+getInstance().getComponent().getId();
	}
	
	@Override
	protected ComponentParameter createInstance() {
		ComponentParameter instance = new ComponentParameter();
		instance.setComponent(new com.esoft.archer.theme.model.Component());
		return instance;
	}
}
