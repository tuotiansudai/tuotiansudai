package com.ttsd.special.dto;

import com.ttsd.special.model.InvestLotteryPrizeType;

import java.io.Serializable;


public class LotteryPrizeResponseDto implements Serializable{
    private int remainingTimes;
    private InvestLotteryPrizeType investLotteryPrizeType;
    private String prizeDesc;

    public int getRemainingTimes() {
        return remainingTimes;
    }

    public void setRemainingTimes(int remainingTimes) {
        this.remainingTimes = remainingTimes;
    }

    public InvestLotteryPrizeType getInvestLotteryPrizeType() {
        return investLotteryPrizeType;
    }

    public void setInvestLotteryPrizeType(InvestLotteryPrizeType investLotteryPrizeType) {
        this.investLotteryPrizeType = investLotteryPrizeType;
    }

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc;
    }
}
