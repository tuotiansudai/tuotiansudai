package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
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

	private static final String LAZY_MODEL = "select referGradeProfitUser from ReferGradeProfitUser referGradeProfitUser ";

	private static final String LAZY_MODEL_COUNT = " select count(referGradeProfitUser) from ReferGradeProfitUser referGradeProfitUser ";



	public ReferGradeProfitUserList(){

		setCountHql(LAZY_MODEL_COUNT);
		setHql(LAZY_MODEL);
		final String[] RESTRICTIONS = {
				"referGradeProfitUser.referrer.id=#{referGradeProfitUserList.referee}",
				"1=1 order by referGradeProfitUser.referrer.id" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));


		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		ReferGradeProfitUser example = new ReferGradeProfitUser();
		example.setReferrer(new User());
		setExample(example);
	}


}
