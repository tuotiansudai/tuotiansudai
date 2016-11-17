package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.activity.dto.LotteryPrize;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryPrizeView implements Serializable {

    private String mobile;

    private String userName;

    private LotteryPrize prize;

    private Date lotteryTime;

    private String prizeValue;

    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

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

    public String getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(String prizeValue) {
        this.prizeValue = prizeValue;
    }
}
