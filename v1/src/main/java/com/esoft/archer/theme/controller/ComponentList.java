package com.esoft.archer.theme.controller;

import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityQuery;

import com.esoft.archer.menu.model.MenuType;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.StringManager;

/**
 * 菜单类型查询
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class ComponentList extends EntityQuery<com.esoft.archer.theme.model.Component> implements java.io.Serializable{
	
	private static final long serialVersionUID = 6648384238107149229L;
	
	static StringManager sm = StringManager.getManager(CommonConstants.Package);
	@Logger static Log log ;
	
	public ComponentList(){
		final String[] RESTRICTIONS = {"id like #{componentList.example.id}",
				"name like #{componentList.example.name}",
				//"scriptUrl like #{componentList.example.scriptUrl}",
				//"enable like #{componentList.example.enable}",
				//"description like #{componentList.example.description}",
				//"componentParameters like #{componentList.example.componentParameters}",
				//"regions like #{componentList.example.regions}",
				};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	
}
