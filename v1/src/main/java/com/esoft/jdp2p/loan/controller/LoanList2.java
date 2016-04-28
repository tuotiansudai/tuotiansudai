package com.esoft.jdp2p.loan.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.controller.LoanList;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-5-14 下午2:16:14
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-5-14 wangzhi 1.0
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanList2 extends LoanList {

	public LoanList2() {
		final String[] RESTRICTIONS = { "loan.id like #{loanList2.example.id}",
				"loan.repayType like #{loanList2.example.repayType}",
				"loan.status like #{loanList2.example.status}",
				"loan.name like #{loanList2.example.name}",
				"loan.rate >=#{loanList2.minRate}",
				"loan.rate <=#{loanList2.maxRate}",
				"loan.status like #{loanList2.example.status}",
				"loan.riskLevel like #{loanList2.example.riskLevel}",
				"loan.type like #{loanList2.example.type}",
				"loan.user.id = #{loanList2.example.user.id}",
				"loan.user.username like #{loanList2.example.user.username}",
				"loan.businessType like #{loanList2.example.businessType}",
				"loan.deadline >= #{loanList2.minDeadline}",
				"loan.deadline <= #{loanList2.maxDeadline}",
				"loan.money >= #{loanList2.minMoney}",
				"loan.money <= #{loanList2.maxMoney}",
				"loan.loanMoney >= #{loanList2.minLoanMoney}",
				"loan.loanMoney <= #{loanList2.maxLoanMoney}",
				"loan.commitTime >= #{loanList2.searchCommitMinTime}",
				"loan.commitTime <= #{loanList2.searchCommitMaxTime}",
				"loan.loanPurpose like #{loanList2.example.loanPurpose}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

}
