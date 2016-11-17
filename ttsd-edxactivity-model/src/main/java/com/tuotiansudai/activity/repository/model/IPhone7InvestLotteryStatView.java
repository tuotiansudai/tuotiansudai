package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;

public class IPhone7InvestLotteryStatView implements Serializable {
    private String loginName;
    private long investAmountTotal;
    private int investCount;
    private String lotteryNumberArray;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getInvestAmountTotal() {
        return investAmountTotal;
    }

    public void setInvestAmountTotal(long investAmountTotal) {
        this.investAmountTotal = investAmountTotal;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public String getLotteryNumberArray() {
        return lotteryNumberArray;
    }

    public void setLotteryNumberArray(String lotteryNumberArray) {
        this.lotteryNumberArray = lotteryNumberArray;
    }
}


