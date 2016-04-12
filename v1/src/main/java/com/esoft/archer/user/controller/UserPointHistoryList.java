package com.esoft.archer.user.controller;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserPointHistory;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.DateUtil;

@Component
@Scope(ScopeType.VIEW)
public class UserPointHistoryList extends EntityQuery<UserPointHistory> {

	public UserPointHistoryList() {
		final String[] RESTRICTIONS = {
				"userPointHistory.id like #{userPointHistoryList.example.id}",
				"userPointHistory.operateType like #{userPointHistoryList.example.operateType}",
				"userPointHistory.time >= #{userPointHistoryList.startTime}",
				"userPointHistory.time <= #{userPointHistoryList.endTime}",
				"userPointHistory.user.id like #{userPointHistoryList.example.user.id}",
				"userPointHistory.type like #{userPointHistoryList.example.type}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		UserPointHistory example = new UserPointHistory();
		example.setUser(new User());
		setExample(example);
	}

	private Date startTime;
	
	private Date endTime;
	
	
	/**
	 * 设置查询的起始和结束时间
	 */
	public void setSearchStartEndTime(Date startTime, Date endTime) {
		this.startTime = startTime;
		if (endTime != null) {
			this.endTime = DateUtil.addDay(endTime, 1);
		}
	}
	/**
	 * @author hch
	 * @return 根据指定用户获取总积分
	 */
	public long getUserSumPoint(String id) {
		if (id != null && id.length() > 0) {
			StringBuilder sql = new StringBuilder(
					"select sum(h.point) from UserPointHistory h where h.user.id=?");
			Long sum = (Long) getHt().find(sql.toString(), id).get(0);
			if (sum != null) {
				return sum;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * @author hch
	 * @param type
	 * @return 根据积分类型找到对应的积分类型名称
	 */
	public String getTypeName(String type) {
		return UserConstants.UserPointType.historyTypeMap.get(type);
	}

	/**
	 * @author hch
	 * @param value
	 * @return 为了获取字符串的长度，为了方便页面调用
	 */
	public int getStrLength(String value) {
		if (value != null) {
			return value.length();
		} else {
			return 0;
		}
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
