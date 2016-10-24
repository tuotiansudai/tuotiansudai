package com.tuotiansudai.activity.model;


public class LotteryPrizeView {

    private String lotteryPrize;

    private String lotteryPrizeName;

    public LotteryPrizeView(String lotteryPrize, String lotteryPrizeName) {
        this.lotteryPrize = lotteryPrize;
        this.lotteryPrizeName = lotteryPrizeName;
    }

    public String getLotteryPrize() {
        return lotteryPrize;
    }

    public void setLotteryPrize(String lotteryPrize) {
        this.lotteryPrize = lotteryPrize;
    }

    public String getLotteryPrizeName() {
        return lotteryPrizeName;
    }

    public void setLotteryPrizeName(String lotteryPrizeName) {
        this.lotteryPrizeName = lotteryPrizeName;
    }
}
