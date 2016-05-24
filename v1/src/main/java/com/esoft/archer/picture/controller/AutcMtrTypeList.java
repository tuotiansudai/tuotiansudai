package com.esoft.archer.picture.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.picture.model.AutcMtrType;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;

/**
 * 认证材料查询List
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class AutcMtrTypeList extends EntityQuery<AutcMtrType> {
	
	
	@Logger
	static Log log;
	private static final String[] RESTRICTIONS = {
			"autcMtrType.id like #{autcMtrTypeList.example.id}",
			"autcMtrType.name like #{autcMtrTypeList.example.name}"};
	
	private static final String lazyModelCountHql = "select count(distinct autcMtrType) from AutcMtrType autcMtrType ";
	private static final String lazyModelHql = "select distinct autcMtrType from AutcMtrType autcMtrType ";
	public AutcMtrTypeList() {
		// TODO Auto-generated constructor stub
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	/**
	 * 通过id查找认证材料
	 * @param pid
	 * @return
	 */
	public AutcMtrType getAutcTypeById(String pid){
		
		return getHt().get(AutcMtrType.class, pid);
	}
}
