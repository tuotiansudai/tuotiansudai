package com.esoft.archer.system.controller;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.model.EntityGroupBy;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;

/**
 * 动作追踪，根据某一项groupBy查询
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class MotionTrackingGroupByList extends EntityQuery<EntityGroupBy> {

	@Logger
	static Log log;

	private String groupByField;

	private static final String[] RESTRICTIONS = { "motionTracking.fromType = #{motionTrackingGroupByList.example.fromType}" };

	public void init() {
		if (StringUtils.isEmpty(groupByField)) {
			throw new RuntimeException("groupByField is empty");
		}
		String lazyModelCountHql = "select count(motionTracking) from MotionTracking motionTracking group by motionTracking."
				+ groupByField;
		String lazyModelHql = "select "
				+ "new com.esoft.archer.system.model.EntityGroupBy"
				+ "("
				+ "'com.esoft.archer.system.model.MotionTracking',"
				+ "'" + groupByField + "',"
				+ "count(mt.id),"
				+ "null"
				+ ")"
				+ " from MotionTracking motionTracking group by motionTracking."
				+ groupByField;
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public String getGroupByField() {
		return groupByField;
	}

	public void setGroupByField(String groupByField) {
		this.groupByField = groupByField;
	}

}
