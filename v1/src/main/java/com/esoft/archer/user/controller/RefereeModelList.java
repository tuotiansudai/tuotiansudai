package com.esoft.archer.user.controller;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.RefereeModel;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;

/**
 * 
 * @author hch 推荐人模型控制器
 */
@Component
@Scope(ScopeType.REQUEST)
public class RefereeModelList extends EntityQuery<RefereeModel> {
	private static final String lazyModelCount = "select "
			+ "count(user.referrer) "
			+ "from Invest invest inner join invest.user user "
			+ "where user.referrer != null and " + "invest.status in ('"
			+ InvestStatus.BID_SUCCESS + "','" + InvestStatus.REPAYING + "','"
			+ InvestStatus.OVERDUE + "','" + InvestStatus.COMPLETE + "','"
			+ InvestStatus.BAD_DEBT + "')";

	// 查询数据如果为空会报错，需要修改
	private static final String lazyModel = "select "
			+ "new com.esoft.archer.user.model.RefereeModel(user.referrer,sum(invest.money),min(invest.time),max(invest.time)) "
			+ "from Invest invest inner join invest.user user "
			+ "where user.referrer is not null and user.referrer != '' and "
			+ "invest.status in ('" + InvestStatus.BID_SUCCESS + "','"
			+ InvestStatus.REPAYING + "','" + InvestStatus.OVERDUE + "','"
			+ InvestStatus.COMPLETE + "','" + InvestStatus.BAD_DEBT + "')";

	// 查询条件 start
	private String referee;// 推荐人
	private Date searchCommitMinTime, searchCommitMaxTime;

	// 查询条件 end

	public RefereeModelList() {
		setCountHql(lazyModelCount);
		setHql(lazyModel);
		final String[] RESTRICTIONS = {
				"invest.time >= #{refereeModelList.searchCommitMinTime}",
				"invest.time <= #{refereeModelList.searchCommitMaxTime}",
				"user.referrer=#{refereeModelList.referee}",
				"1=1 group by user.referrer" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public Date getSearchCommitMinTime() {
		return searchCommitMinTime;
	}

	public void setSearchCommitMinTime(Date searchCommitMinTime) {
		this.searchCommitMinTime = searchCommitMinTime;
	}

	public Date getSearchCommitMaxTime() {
		return searchCommitMaxTime;
	}

	public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
		this.searchCommitMaxTime = searchCommitMaxTime;
	}
}
