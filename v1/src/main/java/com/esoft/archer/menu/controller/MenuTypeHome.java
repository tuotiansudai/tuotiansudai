package com.esoft.archer.menu.controller;


import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.menu.MenuConstants;
import com.esoft.archer.menu.model.Menu;
import com.esoft.archer.menu.model.MenuType;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class MenuTypeHome extends EntityHome<MenuType>{
	
	@Logger static Log log ;
	private static final StringManager sm = StringManager.getManager(MenuConstants.Package);
	public MenuTypeHome(){
		setUpdateView( FacesUtil.redirect(MenuConstants.View.MENU_TYPE_LIST) );
	}

	@Override
	@Transactional(readOnly=false)
	public String delete() {
		if(log.isInfoEnabled()){
			log.info(sm.getString("log.info.deleteMenuType",FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
		}
		Set<Menu> menuSets = getInstance().getMenus();
		if(menuSets != null && menuSets.size() > 0){
			FacesUtil.addWarnMessage(sm.getString("canNotDeleteMenuType"));
			if(log.isInfoEnabled()){
				log.info(sm.getString("log.info.deleteMenuUnsuccessful",
						FacesUtil
						.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
			}
			return null;
		}else{
			return super.delete();
		}
	}
}
