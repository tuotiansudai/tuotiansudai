package com.esoft.jdp2p.loan.controller;

import com.esoft.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/7/31.
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanListRaising extends LoanList {

    public LoanListRaising() {
        final String[] RESTRICTIONS = { "loan.id like #{LoanListRaising.example.id}",
                "loan.repayType like #{LoanListRaising.example.repayType}",
                "loan.status like #{LoanListRaising.example.status}",
                "loan.name like #{LoanListRaising.example.name}",
                "loan.rate >=#{LoanListRaising.minRate}",
                "loan.rate <=#{LoanListRaising.maxRate}",
                "loan.status like #{LoanListRaising.example.status}",
                "loan.riskLevel like #{LoanListRaising.example.riskLevel}",
                "loan.type like #{LoanListRaising.example.type}",
                "loan.user.id = #{LoanListRaising.example.user.id}",
                "loan.user.username like #{LoanListRaising.example.user.username}",
                "loan.businessType like #{LoanListRaising.example.businessType}",
                "loan.deadline >= #{LoanListRaising.minDeadline}",
                "loan.deadline <= #{LoanListRaising.maxDeadline}",
                "loan.money >= #{LoanListRaising.minMoney}",
                "loan.money <= #{LoanListRaising.maxMoney}",
                "loan.loanMoney >= #{LoanListRaising.minLoanMoney}",
                "loan.loanMoney <= #{LoanListRaising.maxLoanMoney}",
                "loan.commitTime >= #{LoanListRaising.searchCommitMinTime}",
                "loan.commitTime <= #{LoanListRaising.searchCommitMaxTime}",
                "loan.loanPurpose like #{LoanListRaising.example.loanPurpose}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
