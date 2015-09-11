package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.model.ReferrerInvest;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.core.annotations.ScopeType;
import com.google.common.collect.Lists;
import com.ttsd.special.model.InvestLottery;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Scope(ScopeType.VIEW)
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
//	@Override
//	protected void initExample() {
//		InvestLottery example = new InvestLottery();
//		example.setUser(new User());
//		setExample(example);
//	}

//	public List<InvestLottery> getInvestLotteryList(boolean flag){
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		List<InvestLottery> listResult = Lists.newArrayList();
//		String sql = " select `invest_id` AS `investId` ,"
//				+ "`user_id` AS userId,`type` AS `type`,"
//				+ "`created_time` AS `createdTime`,"
//				+ "`prize_type` AS `prizeType`, "
//				+ " `amount` AS `amount`,"
//				+ " `award_time` AS `awardTime`,"
//				+ "`in_valid` AS `valid` "
//				+ " from `invest_lottery` where 1=1  ";
//				if (startTime != null) {
//					sql += " AND `award_time` >= ''"+simpleDateFormat.format(startTime)+"'' ";
//				}
//				if (endTime != null) {
//					sql += " AND `award_time` <= ''"+simpleDateFormat.format(endTime)+"'' ";
//				}
//				if(flag){
//					sql +=  " LIMIT " + (index - 1) * 10 + "," + investLotteryPageSize;
//				}
//				sql += " ORDER BY created_time desc";
//
//		Query query = getHt().getSessionFactory().getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//		List<Map<String, Object>> result = query.list();
//		for (int i=0;i<result.size();i++) {
//
//
//		}
//		return listResult;
//	}
}
