package com.tuotiansudai.activity.repository.dto;

import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;

import java.io.Serializable;

public class IPhone7LotteryDto implements Serializable {
    private String lotteryNumber;
    private String mobile;

    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public IPhone7LotteryDto(IPhone7LotteryConfigModel iPhone7LotteryConfigModel, String mobile){
        this.lotteryNumber = iPhone7LotteryConfigModel.getLotteryNumber();
        this.mobile = mobile;
    }


}

