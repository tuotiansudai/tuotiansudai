package com.tuotiansudai.activity.dto;

import java.io.Serializable;

public class UserLotteryDto implements Serializable{

    private int lotteryTime;

    private LotteryTaskStatus registerStatus;

    private LotteryTaskStatus certificationStatus;

    private LotteryTaskStatus bindCardStatus;

    private LotteryTaskStatus rechargeStatus;

    private LotteryTaskStatus investStatus;

    public UserLotteryDto() {}

    public UserLotteryDto(int lotteryTime,LotteryTaskStatus lotteryTaskStatus) {
        this.lotteryTime = lotteryTime;
        this.rechargeStatus = lotteryTaskStatus;
        this.certificationStatus = lotteryTaskStatus;
        this.bindCardStatus = lotteryTaskStatus;
        this.rechargeStatus = lotteryTaskStatus;
        this.investStatus = lotteryTaskStatus;
    }

    public int getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(int lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public LotteryTaskStatus getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(LotteryTaskStatus registerStatus) {
        this.registerStatus = registerStatus;
    }

    public LotteryTaskStatus getCertificationStatus() {
        return certificationStatus;
    }

    public void setCertificationStatus(LotteryTaskStatus certificationStatus) { this.certificationStatus = certificationStatus; }

    public LotteryTaskStatus getBindCardStatus() {
        return bindCardStatus;
    }

    public void setBindCardStatus(LotteryTaskStatus bindCardStatus) {
        this.bindCardStatus = bindCardStatus;
    }

    public LotteryTaskStatus getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(LotteryTaskStatus rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public LotteryTaskStatus getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(LotteryTaskStatus investStatus) {
        this.investStatus = investStatus;
    }
}
