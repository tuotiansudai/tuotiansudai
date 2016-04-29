package com.esoft.archer.user.controller;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.UserConstants.UserPointOperateType;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.model.UserPoint;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.VIEW)
public class UserPointList extends EntityQuery<UserPoint> {

	public UserPointList() {
		final String[] RESTRICTIONS = { "id like #{userPointList.example.id}",
				"user.username like #{userPointList.example.user.username}",
				"type like #{userPointList.example.type}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		UserPoint example = new UserPoint();
		example.setUser(new User());
		setExample(example);
	}

	/**
	 * 获取用户的已使用积分
	 * 
	 * @param userId
	 * @param type
	 *            积分类型
	 * @return
	 */
	public Long getUsedPoints(String userId, String type) {
		String hql = "select sum(uph.point) from UserPointHistory uph where uph.type=? and uph.operateType=? and uph.user.id = ?";
		Object o = getHt().find(hql,
				new String[] { type, UserPointOperateType.MINUS, userId }).get(
				0);
		if (o == null) {
			return 0L;
		}
		return (Long) o;
	}

}
