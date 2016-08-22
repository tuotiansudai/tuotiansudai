package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryPrizeView implements Serializable {

    private String mobile;

    private String userName;

    private LotteryPrize prize;

    private Date lotteryTime;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LotteryPrize getPrize() {
        return prize;
    }

    public void setPrize(LotteryPrize prize) {
        this.prize = prize;
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }
}
