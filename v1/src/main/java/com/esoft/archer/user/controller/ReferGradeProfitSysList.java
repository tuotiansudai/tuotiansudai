package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Scope(ScopeType.REQUEST)
public class ReferGradeProfitSysList extends EntityQuery<ReferGradeProfitSys> {


	private static final String lazyModel = "select referGradeProfitSys from ReferGradeProfitSys referGradeProfitSys ";

	private static final String lazyModelCount = " select count(referGradeProfitSys) from ReferGradeProfitSys referGradeProfitSys ";



	public ReferGradeProfitSysList(){

		setCountHql(lazyModelCount);
		setHql(lazyModel);
		final String[] RESTRICTIONS = {
				"1=1 order by referGradeProfitSys.grade" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}


}
