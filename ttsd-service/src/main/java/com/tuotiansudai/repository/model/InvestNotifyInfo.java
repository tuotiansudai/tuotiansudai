package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class InvestNotifyInfo implements Serializable {
    /**
     * 标的ID
     */
    private String loanName;
    /**
     * 投资金额
     */
    private long amount;
    /**
     * 投资人手机号
     */
    private String mobile;
    /**
     * 投资人邮箱
     */
    private String email;

    private long investId;

    private int periods;

    private String loginName;


    public InvestNotifyInfo(InvestModel investModel, LoanModel loanModel, UserModel userModel){
        this.loanName = loanModel.getName();
        this.amount = investModel.getAmount();
        this.investId = investModel.getId();
        this.loginName = investModel.getLoginName();
        this.mobile = userModel.getMobile();
        this.email = userModel.getEmail();
        this.periods = loanModel.getPeriods();
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
