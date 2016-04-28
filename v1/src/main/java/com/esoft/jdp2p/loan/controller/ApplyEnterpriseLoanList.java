package com.esoft.jdp2p.loan.controller;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.ApplyEnterpriseLoan;
import com.esoft.jdp2p.loan.model.Loan;

/**
 * Description: 企业借款申请List
 * 
 * Copyright: Copyright (c)2013 Company: jdp2p
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-10 上午9:31:08
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-10 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class ApplyEnterpriseLoanList extends EntityQuery<ApplyEnterpriseLoan> {

	public ApplyEnterpriseLoanList() {
		final String[] RESTRICTIONS = {
				"user.id = #{applyEnterpriseLoanList.example.user.id}",
				"applyEnterpriseLoan.type like #{applyEnterpriseLoanList.example.type}",
				"applyEnterpriseLoan.applyTime >= #{applyEnterpriseLoanList.searchcommitMinTime}",
				"applyEnterpriseLoan.applyTime <= #{applyEnterpriseLoanList.searchcommitMaxTime}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		ApplyEnterpriseLoan example = new ApplyEnterpriseLoan();
		example.setUser(new User());
		setExample(example);
	}

	private Date searchcommitMinTime;// 最小申请时间
	private Date searchcommitMaxTime;// 最大申请时间

	public Date getSearchcommitMinTime() {
		return searchcommitMinTime;
	}

	public void setSearchcommitMinTime(Date searchcommitMinTime) {
		this.searchcommitMinTime = searchcommitMinTime;
	}

	public Date getSearchcommitMaxTime() {
		return searchcommitMaxTime;
	}

	public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
		this.searchcommitMaxTime = searchcommitMaxTime;
	}

	/**
	 * 设置查询的起始和结束时间 查询条件：申请时间
	 */
	public void setSearchStartEndTime(Date startTime, Date endTime) {
		this.searchcommitMinTime = startTime;
		this.searchcommitMaxTime = endTime;
	}
}
