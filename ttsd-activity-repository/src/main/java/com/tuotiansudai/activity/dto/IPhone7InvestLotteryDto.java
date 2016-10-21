package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatus;

import java.util.Date;

/**
 * Created by gengbeijun on 16/10/17.
 */
public class IPhone7InvestLotteryDto {

    private String lotteryNumber;

    private String status;


    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IPhone7InvestLotteryDto(IPhone7InvestLotteryModel iPhone7InvestLotteryModel, boolean isExpire){
        this.lotteryNumber = iPhone7InvestLotteryModel.getLotteryNumber();
        this.status = iPhone7InvestLotteryModel.getStatus() == IPhone7InvestLotteryStatus.WAITING ? isExpire ? IPhone7InvestLotteryStatus.WAITING.getDescription() : "未中奖" : IPhone7InvestLotteryStatus.WINNING.getDescription();
    }
}
