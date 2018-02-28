package com.tuotiansudai.activity.repository.dto;


import com.tuotiansudai.activity.repository.model.UserExchangePrizeModel;

import java.util.Date;

public class StartWorkPrizeDto {

    private String mobile;
    private String userName;
    private String prize;
    private Date exchangeTime;
    private int count;

    public StartWorkPrizeDto() {
    }

    public StartWorkPrizeDto(UserExchangePrizeModel userExchangePrizeModel){
        this.mobile = userExchangePrizeModel.getMobile();
        this.userName = userExchangePrizeModel.getUserName();
        this.prize = userExchangePrizeModel.getPrize().getPrizeName();
        this.exchangeTime = userExchangePrizeModel.getExchangeTime();
        this.count = (int) (userExchangePrizeModel.getPrize().getExchangeMoney() / 5000000);
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

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
