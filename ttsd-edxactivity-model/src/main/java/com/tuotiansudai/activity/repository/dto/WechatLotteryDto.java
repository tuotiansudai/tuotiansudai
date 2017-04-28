package com.tuotiansudai.activity.repository.dto;

import com.tuotiansudai.activity.repository.model.WechatLotteryPrize;
import com.tuotiansudai.dto.BaseDataDto;

public class WechatLotteryDto extends BaseDataDto {

    private int returnCode;

    private WechatLotteryPrize wechatLotteryPrize;

    private int leftDrawCount;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public WechatLotteryPrize getWechatLotteryPrize() {
        return wechatLotteryPrize;
    }

    public void setWechatLotteryPrize(WechatLotteryPrize wechatLotteryPrize) {
        this.wechatLotteryPrize = wechatLotteryPrize;
    }

    public int getLeftDrawCount() {
        return leftDrawCount;
    }

    public void setLeftDrawCount(int leftDrawCount) {
        this.leftDrawCount = leftDrawCount;
    }
}
