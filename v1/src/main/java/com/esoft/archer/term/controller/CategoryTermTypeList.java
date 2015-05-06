package com.esoft.archer.term.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.term.TermConstants;
import com.esoft.archer.term.model.CategoryTermType;
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
public class CategoryTermTypeList extends EntityQuery<CategoryTermType> {
	
	static StringManager sm = StringManager.getManager(TermConstants.Package);
	@Logger static Log log ;
	
	public CategoryTermTypeList(){
		final String[] RESTRICTIONS = 
				{"id like #{categoryTermTypeList.example.id}",
				"name like #{categoryTermTypeList.example.name}",
				"description like #{categoryTermTypeList.example.description}"};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	
}
