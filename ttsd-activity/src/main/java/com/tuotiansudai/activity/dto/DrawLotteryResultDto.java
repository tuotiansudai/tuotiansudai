package com.tuotiansudai.activity.dto;


import com.tuotiansudai.dto.BaseDataDto;

public class DrawLotteryResultDto extends BaseDataDto {

    private int returnCode;

    private LotteryPrize lotteryPrize;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public LotteryPrize getLotteryPrize() {
        return lotteryPrize;
    }

    public void setLotteryPrize(LotteryPrize lotteryPrize) {
        this.lotteryPrize = lotteryPrize;
    }
}
