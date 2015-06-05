package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class ReferrerRelationList extends EntityQuery<User> {

	@Resource
	private LoginUserInfo loginUserInfo;

//	private static final String LAZY_MODEL = "select new com.esoft.archer.user.model.ReferrerRelation(referrerId,userId) from ReferrerRelation referrerRelation ";
	private static final String LAZY_MODEL = "select distinct user from User user inner join user.referrers referrer where referrer.id=''{0}'' ";

	private static final String LAZY_MODEL_COUNT = "select count(distinct user) from User user inner join user.referrers referrer where referrer.id=''{0}'' ";

	private static final String QUERY_LEVEL = "select relation.level from ReferrerRelation relation where relation.referrerId=''{0}'' and relation.userId=''{1}''";


	public ReferrerRelationList(){
//		setCountHql(MessageFormat.format(LAZY_MODEL_COUNT, loginUserInfo.getLoginUserId()));
//		setHql(MessageFormat.format(LAZY_MODEL, loginUserInfo.getLoginUserId()));
//		final String[] RESTRICTIONS = {
//				"referrerRelation.referrerId = #{referrerRelation.example.referrer.id}",
//				"1=1 order by referrerRelation.user.id " };
//		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}


	@Override
	public List<User> getLazyModelData() {
		setCountHql(MessageFormat.format(LAZY_MODEL_COUNT, loginUserInfo.getLoginUserId()));
		setHql(MessageFormat.format(LAZY_MODEL, loginUserInfo.getLoginUserId()));
		return super.getLazyModelData();
	}

	public List<ReferrerRelation> getReferrerRelations() {
		String referrerId = loginUserInfo.getLoginUserId();
		List<User> refereeList = this.getLazyModelData();
		List<ReferrerRelation> relations = Lists.newArrayList();
		for (User referee : refereeList) {
			ReferrerRelation relation = new ReferrerRelation();
			Integer level = (Integer) getHt().find(MessageFormat.format(QUERY_LEVEL, referrerId, referee.getId())).get(0);
			relation.setUser(referee);
			relation.setLevel(level);
			relations.add(relation);
		}
		return relations;
	}

}
