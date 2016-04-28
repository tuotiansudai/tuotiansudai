package com.esoft.archer.menu.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.menu.model.MenuType;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.StringManager;

/**
 * 菜单类型查询
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class MenuTypeList extends EntityQuery<MenuType> implements java.io.Serializable{
	
	static StringManager sm = StringManager.getManager(CommonConstants.Package);
	
//	private static 
	
	public MenuTypeList(){
		final String[] RESTRICTIONS = {
				"id like #{menuTypeList.example.id}",
				"name like #{menuTypeList.example.name}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	
}
