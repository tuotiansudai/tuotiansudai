package com.esoft.archer.system.controller;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.model.MotionTracking;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.REQUEST)
public class MotionTrackingList extends EntityQuery<MotionTracking> {

	public List<MotionTracking> getMotionTrackingsByWhoFromType(String who,
			String fromType) {
		return getHt().find(
				"from MotionTracking mt where mt.who = ? and mt.fromType = ?",
				new String[] { who, fromType });
	}

}
