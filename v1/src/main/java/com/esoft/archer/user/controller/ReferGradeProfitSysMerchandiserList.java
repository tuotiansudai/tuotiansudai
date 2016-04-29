package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Scope(ScopeType.REQUEST)
public class ReferGradeProfitSysMerchandiserList extends EntityQuery<ReferGradeProfitSys> {


	private static final String LAZY_MODEL = "select referGradeProfitSys from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.gradeRole='ROLE_MERCHANDISER' ";

	private static final String LAZY_MODEL_COUNT = " select count(referGradeProfitSys) from ReferGradeProfitSys referGradeProfitSys where referGradeProfitSys.gradeRole='ROLE_MERCHANDISER' ";



	public ReferGradeProfitSysMerchandiserList(){

		setCountHql(LAZY_MODEL_COUNT);
		setHql(LAZY_MODEL);
		final String[] RESTRICTIONS = {
				"1=1 order by referGradeProfitSys.grade" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}


}
