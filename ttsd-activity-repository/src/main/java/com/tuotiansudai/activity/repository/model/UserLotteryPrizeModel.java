package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.activity.dto.PrizeType;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryPrizeModel implements Serializable {

    private long id;

    private String mobile;

    private String loginName;

    private String userName;

    private String prize;

    private Date lotteryTime;

    private PrizeType prizeType;

    public UserLotteryPrizeModel() {}

    public UserLotteryPrizeModel(String mobile, String loginName,String userName, String prize, Date lotteryTime,PrizeType prizeType) {
        this.mobile = mobile;
        this.loginName = loginName;
        this.userName = userName;
        this.prize = prize;
        this.lotteryTime = lotteryTime;
        this.prizeType = prizeType;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PrizeType getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(PrizeType prizeType) {
        this.prizeType = prizeType;
    }
}
