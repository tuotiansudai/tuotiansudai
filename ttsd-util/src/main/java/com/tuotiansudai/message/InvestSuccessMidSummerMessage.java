package com.tuotiansudai.message;


import java.io.Serializable;
import java.util.Date;

public class InvestSuccessMidSummerMessage implements Serializable {

    private long investId;

    private String loginName;

    private String referrerLoginName;

    private long amount;

    private Date tradingTime;

    public InvestSuccessMidSummerMessage() {

    }

    public InvestSuccessMidSummerMessage(long investId, String loginName, String referrerLoginName, long amount, Date tradingTime) {
        this.investId = investId;
        this.loginName = loginName;
        this.referrerLoginName = referrerLoginName;
        this.amount = amount;
        this.tradingTime = tradingTime;
    }

    public long getInvestId() {
        return investId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public long getAmount() {
        return amount;
    }

    public Date getTradingTime() {
        return tradingTime;
    }
}
