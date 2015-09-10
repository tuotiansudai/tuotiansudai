package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.model.ReferrerInvest;
import com.esoft.core.annotations.ScopeType;
import com.google.common.collect.Lists;
import com.ttsd.special.model.InvestLottery;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.REQUEST)
public class InvestLotteryList extends EntityQuery<InvestLottery> implements Serializable {
	private Date startTime;
	private Date endTime;

	public InvestLotteryList(){
		final String[] RESTRICTIONS = {
				"user = #{investLotteryList.example.user}",
				"valid = #{investLotteryList.example.valid}",
				"awardTime >= #{investLotteryList.startTime}",
				"awardTime <= #{investLotteryList.endTime}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
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

//	public List<InvestLottery> getInvestLotteryList(boolean flag){
//		List<ReferrerInvest> listResult = Lists.newArrayList();
//		String sql = " select `invest_id` AS `investId` ,"
//				+ "`user_id` AS userId,`type` AS `type`,"
//				+ "`created_time` AS `createdTime`,"
//				+ "`prize_type` AS `prizeType`, "
//				+ " `amount` AS `amount`,"
//				+ " award_time,in_valid from ";
//	}
}
