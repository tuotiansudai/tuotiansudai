package com.esoft.archer.theme.controller;


import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Region;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.REQUEST)
public class RegionHome extends EntityHome<Region>{
	
	@Logger static Log log ;
	
	public RegionHome(){
		setUpdateView( FacesUtil.redirect(ThemeConstants.View.REGION_LIST) );
	}

	
}
