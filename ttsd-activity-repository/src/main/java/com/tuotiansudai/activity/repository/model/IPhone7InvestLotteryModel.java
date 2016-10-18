package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class IPhone7InvestLotteryModel implements Serializable {
    private long id;
    private long investId;
    private String loginName;
    private String lotteryNumber;
    private long investAmount;
    private Date lotteryTime;
    private IPhone7InvestLotteryStatus status;

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

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public IPhone7InvestLotteryStatus getStatus() {
        return status;
    }

    public void setStatus(IPhone7InvestLotteryStatus status) {
        this.status = status;
    }


    public IPhone7InvestLotteryModel(long investId, String loginName, long investAmount, String lotteryNumber){
        this.investId = investId;
        this.loginName = loginName;
        this.investAmount = investAmount;
        this.lotteryNumber = lotteryNumber;
    }
}


