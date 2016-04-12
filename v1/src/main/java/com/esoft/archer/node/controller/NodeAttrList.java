package com.esoft.archer.node.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.NodeAttr;
import com.esoft.archer.node.model.NodeType;
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
public class NodeAttrList extends EntityQuery<NodeAttr> {
	
	
	static StringManager sm = StringManager.getManager(NodeConstants.Package);
	@Logger static Log log ;
	
	public NodeAttrList(){
		final String[] RESTRICTIONS = 
				{"id like #{nodeAttrList.example.id}",
				"name like #{nodeAttrList.example.name}"};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	
}
