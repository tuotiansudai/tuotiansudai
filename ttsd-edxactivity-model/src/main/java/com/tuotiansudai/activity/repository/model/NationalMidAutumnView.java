package com.tuotiansudai.activity.repository.model;

import java.util.Date;

public class NationalMidAutumnView {

    private String userName;
    private String loginName;
    private String mobile;
    private long sumCashInvestAmount;
    private long sumCouponInvestAmount;
    private long sumMoneyAmount;

    public NationalMidAutumnView() {
    }

    public NationalMidAutumnView(String userName, String loginName,String mobile, long sumCashInvestAmount, long sumCouponInvestAmount, long sumMoneyAmount) {
        this.userName = userName;
        this.loginName = loginName;
        this.mobile = mobile;
        this.sumCashInvestAmount = sumCashInvestAmount;
        this.sumCouponInvestAmount = sumCouponInvestAmount;
        this.sumMoneyAmount = sumMoneyAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getSumCashInvestAmount() {
        return sumCashInvestAmount;
    }

    public void setSumCashInvestAmount(long sumCashInvestAmount) {
        this.sumCashInvestAmount = sumCashInvestAmount;
    }

    public long getSumCouponInvestAmount() {
        return sumCouponInvestAmount;
    }

    public void setSumCouponInvestAmount(long sumCouponInvestAmount) {
        this.sumCouponInvestAmount = sumCouponInvestAmount;
    }

    public long getSumMoneyAmount() {
        return sumMoneyAmount;
    }

    public void setSumMoneyAmount(long sumMoneyAmount) {
        this.sumMoneyAmount = sumMoneyAmount;
    }
}
