package com.tuotiansudai.activity.repository.model;

import java.util.Date;

public class NationalMidAutumnModel {

    private long id;
    private long loanId;
    private String loginName;
    private String userName;
    private String mobile;
    private long investCash;
    private long investCoupon;
    private long moneyAmount;
    private NationalMidAutumnLoanType loanType;
    private Date createdTime;

    public NationalMidAutumnModel() {
    }

    public NationalMidAutumnModel(long loanId, String loginName, String userName, String mobile, long investCash, long investCoupon, long moneyAmount, NationalMidAutumnLoanType loanType, Date createdTime) {
        this.loanId = loanId;
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.investCash = investCash;
        this.investCoupon = investCoupon;
        this.moneyAmount = moneyAmount;
        this.loanType = loanType;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getInvestCash() {
        return investCash;
    }

    public void setInvestCash(long investCash) {
        this.investCash = investCash;
    }

    public long getInvestCoupon() {
        return investCoupon;
    }

    public void setInvestCoupon(long investCoupon) {
        this.investCoupon = investCoupon;
    }

    public long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public NationalMidAutumnLoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(NationalMidAutumnLoanType loanType) {
        this.loanType = loanType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
