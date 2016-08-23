package com.tuotiansudai.console.activity.dto;

import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class UserLuxuryPrizeDto implements Serializable{

    private static final long serialVersionUID = 4130121034168192590L;

    private Date createdTime;

    private String prize;

    private String mobile;

    private String userName;

    private String investAmount;

    public UserLuxuryPrizeDto(){

    }

    public UserLuxuryPrizeDto(UserLuxuryPrizeModel userLuxuryPrizeModel){
        this.createdTime = userLuxuryPrizeModel.getCreatedTime();
        this.prize = userLuxuryPrizeModel.getPrize();
        this.userName = userLuxuryPrizeModel.getUserName();
        this.mobile = userLuxuryPrizeModel.getMobile();
        this.investAmount = AmountConverter.convertCentToString(userLuxuryPrizeModel.getInvestAmount());
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
