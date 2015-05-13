package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.core.annotations.ScopeType;
import com.esoft.archer.user.model.ReferGradeProfitUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Scope(ScopeType.REQUEST)
public class ReferGradeProfitUserList extends EntityQuery<ReferGradeProfitUser> {

	private String referee;// 推荐人

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	private static final String lazyModel = "select referGradeProfitUser from ReferGradeProfitUser referGradeProfitUser ";

	private static final String lazyModelCount = " select count(referGradeProfitUser) from ReferGradeProfitUser referGradeProfitUser ";



	public ReferGradeProfitUserList(){

		setCountHql(lazyModelCount);
		setHql(lazyModel);
		final String[] RESTRICTIONS = {
				"referGradeProfitUser.referrerid=#{referGradeProfitUserList.referee}",
				"1=1 order by referGradeProfitUser.referrerid" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}


}
