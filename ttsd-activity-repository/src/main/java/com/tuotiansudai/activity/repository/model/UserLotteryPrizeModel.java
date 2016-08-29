package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.activity.dto.LotteryPrize;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryPrizeModel implements Serializable {

    private long id;

    private String mobile;

    private String loginName;

    private LotteryPrize prize;

    private Date lotteryTime;

    public UserLotteryPrizeModel() {}

    public UserLotteryPrizeModel(String mobile, String loginName, LotteryPrize prize, Date lotteryTime) {
        this.mobile = mobile;
        this.loginName = loginName;
        this.prize = prize;
        this.lotteryTime = lotteryTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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
