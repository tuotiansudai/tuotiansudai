package com.tuotiansudai.repository.model;

import java.util.Date;

public class InvestPaginationItemView extends InvestModel {

    private String loanName;

    private LoanStatus loanStatus;

    private LoanType loanType;

    private int loanPeriods;

    private String roles;

    private String investorRealName;

    private String investorMobile;

    private String referrerLoginName;

    private String referrerRealName;

    private String referrerMobile;

    private Date nextRepayDate;

    private long nextRepayAmount;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getLoanPeriods() {
        return loanPeriods;
    }

    public void setLoanPeriods(int loanPeriods) {
        this.loanPeriods = loanPeriods;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public long getNextRepayAmount() {
        return nextRepayAmount;
    }

    public void setNextRepayAmount(long nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }

    public String getInvestorRealName() {
        return investorRealName;
    }

    public void setInvestorRealName(String investorRealName) {
        this.investorRealName = investorRealName;
    }

    public String getInvestorMobile() {
        return investorMobile;
    }

    public void setInvestorMobile(String investorMobile) {
        this.investorMobile = investorMobile;
    }

    public String getReferrerRealName() {
        return referrerRealName;
    }

    public void setReferrerRealName(String referrerRealName) {
        this.referrerRealName = referrerRealName;
    }

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }
}
