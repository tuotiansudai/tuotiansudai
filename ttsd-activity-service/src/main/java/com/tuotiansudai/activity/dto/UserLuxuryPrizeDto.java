package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class UserLuxuryPrizeDto implements Serializable{

    private static final long serialVersionUID = 4130121034168192590L;

    private Date createdTime;

    private String name;

    private String mobile;

    private String userName;

    private String investAmount;

    public UserLuxuryPrizeDto(){

    }

    public UserLuxuryPrizeDto(UserLuxuryPrizeModel userLuxuryPrizeModel,String name,String userName){
        this.createdTime = userLuxuryPrizeModel.getCreatedTime();
        this.name = name;
        this.userName = userName;
        this.mobile = userLuxuryPrizeModel.getMobile();
        this.investAmount = AmountConverter.convertCentToString(userLuxuryPrizeModel.getInvestAmount());
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
