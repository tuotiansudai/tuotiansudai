package com.esoft.jdp2p.loan.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.VIEW)
public class LoanList4 extends LoanList{
	public LoanList4() {
		final String[] RESTRICTIONS = { "loan.id like #{loanList4.example.id}",
				"loan.repayType like #{loanList4.example.repayType}",
				"loan.status like #{loanList4.example.status}",
				"loan.name like #{loanList4.example.name}",
				"loan.rate >=#{loanList4.minRate}",
				"loan.rate <=#{loanList4.maxRate}",
				"loan.status like #{loanList4.example.status}",
				"loan.riskLevel like #{loanList4.example.riskLevel}",
				"loan.type like #{loanList4.example.type}",
				"loan.user.id = #{loanList4.example.user.id}",
				"loan.user.username like #{loanList4.example.user.username}",
				"loan.businessType like #{loanList4.example.businessType}",
				"loan.deadline >= #{loanList4.minDeadline}",
				"loan.deadline <= #{loanList4.maxDeadline}",
				"loan.money >= #{loanList4.minMoney}",
				"loan.money <= #{loanList4.maxMoney}",
				"loan.loanMoney >= #{loanList4.minLoanMoney}",
				"loan.loanMoney <= #{loanList4.maxLoanMoney}",
				"loan.commitTime >= #{loanList4.searchCommitMinTime}",
				"loan.commitTime <= #{loanList4.searchCommitMaxTime}",
				"loan.loanPurpose like #{loanList4.example.loanPurpose}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
}
