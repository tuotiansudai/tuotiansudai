package com.esoft.archer.theme.controller;

import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityQuery;

import com.esoft.archer.menu.model.MenuType;
import com.esoft.archer.theme.model.ComponentParameter;
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
public class ComponentParameterList extends EntityQuery<ComponentParameter> implements java.io.Serializable{
	
	private static final long serialVersionUID = 7972343757104858835L;
	
	static StringManager sm = StringManager.getManager(CommonConstants.Package);
	@Logger static Log log ;
	
	public ComponentParameterList(){
		/*final String[] RESTRICTIONS = {"id like #{componentParameterList.example.id}",
				"component like #{componentParameterList.example.component}",
				"name like #{componentParameterList.example.name}",
				"value like #{componentParameterList.example.value}",
				"description like #{componentParameterList.example.description}",
				};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));*/
		
	}
	
	
}
