package com.esoft.archer.node.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.node.NodeConstants;
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
public class NodeTypeList extends EntityQuery<NodeType> implements java.io.Serializable{
	
	private static final long serialVersionUID = 9057256450216810237L;
	
	static StringManager sm = StringManager.getManager(NodeConstants.Package);
	@Logger static Log log ;
	
	public NodeTypeList(){
		final String[] RESTRICTIONS = 
				{"id like #{nodeTypeList.example.id}",
				"name like #{nodeTypeList.example.name}",
				"description like #{nodeTypeList.example.description}"};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	
}
