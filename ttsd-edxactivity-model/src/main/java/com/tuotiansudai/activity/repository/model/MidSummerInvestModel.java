package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class MidSummerInvestModel implements Serializable {

    private long id;

    private long investId;

    private long amount;

    private Date tradingTime;

    private String loginName;

    private String referrerLoginName;

    private Date createdTime;

    public MidSummerInvestModel() {
    }

    public MidSummerInvestModel(long investId, long amount, Date tradingTime, String loginName, String referrerLoginName) {
        this.investId = investId;
        this.amount = amount;
        this.tradingTime = tradingTime;
        this.loginName = loginName;
        this.referrerLoginName = referrerLoginName;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(Date tradingTime) {
        this.tradingTime = tradingTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
