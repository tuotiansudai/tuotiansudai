package com.tuotiansudai.activity.dto;

import com.tuotiansudai.dto.BaseDataDto;

import java.io.Serializable;

public class UserLotteryDto extends BaseDataDto {

    private int lotteryTime;

    private ActivityTaskStatus registerStatus;

    private ActivityTaskStatus certificationStatus;

    private ActivityTaskStatus bindCardStatus;

    private ActivityTaskStatus rechargeStatus;

    private ActivityTaskStatus investStatus;

    public UserLotteryDto() {}

    public UserLotteryDto(int lotteryTime,ActivityTaskStatus activityTaskStatus) {
        this.lotteryTime = lotteryTime;
        this.registerStatus = activityTaskStatus;
        this.certificationStatus = activityTaskStatus;
        this.bindCardStatus = activityTaskStatus;
        this.rechargeStatus = activityTaskStatus;
        this.investStatus = activityTaskStatus;
    }

    public int getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(int lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public ActivityTaskStatus getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(ActivityTaskStatus registerStatus) {
        this.registerStatus = registerStatus;
    }

    public ActivityTaskStatus getCertificationStatus() {
        return certificationStatus;
    }

    public void setCertificationStatus(ActivityTaskStatus certificationStatus) {
        this.certificationStatus = certificationStatus;
    }

    public ActivityTaskStatus getBindCardStatus() {
        return bindCardStatus;
    }

    public void setBindCardStatus(ActivityTaskStatus bindCardStatus) {
        this.bindCardStatus = bindCardStatus;
    }

    public ActivityTaskStatus getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(ActivityTaskStatus rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public ActivityTaskStatus getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(ActivityTaskStatus investStatus) {
        this.investStatus = investStatus;
    }
}
