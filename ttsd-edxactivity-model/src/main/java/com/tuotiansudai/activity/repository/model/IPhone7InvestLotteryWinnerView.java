package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class IPhone7InvestLotteryWinnerView implements Serializable {
    private String loginName;
    private String lotteryNumber;
    private Date effectiveTime;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

}

