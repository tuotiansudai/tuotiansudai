package com.tuotiansudai.activity.repository.dto;

import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.dto.BaseDataDto;

public class WechatLotteryDto extends BaseDataDto {

    private int returnCode;

    private LotteryPrize wechatLotteryPrize;

    private int leftDrawCount;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public LotteryPrize getWechatLotteryPrize() {
        return wechatLotteryPrize;
    }

    public void setWechatLotteryPrize(LotteryPrize wechatLotteryPrize) {
        this.wechatLotteryPrize = wechatLotteryPrize;
    }

    public int getLeftDrawCount() {
        return leftDrawCount;
    }

    public void setLeftDrawCount(int leftDrawCount) {
        this.leftDrawCount = leftDrawCount;
    }
}
