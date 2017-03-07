package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class UserLotteryTop10PrizeView implements Serializable {

    private String mobile;

    private String userName;

    private String prize;

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

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

}
