package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.LotteryTaskStatus;

import java.io.Serializable;

public class UserLotteryDto implements Serializable{

    private String lotteryTime;

    private LotteryTaskStatus registerStatus;

    private LotteryTaskStatus certificationStatus;

    private LotteryTaskStatus bindCardStatus;

    private LotteryTaskStatus rechargeStatus;

    private LotteryTaskStatus investStatus;

    public String getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(String lotteryTime) {
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

    public void setCertificationStatus(LotteryTaskStatus certificationStatus) {
        this.certificationStatus = certificationStatus;
    }

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
