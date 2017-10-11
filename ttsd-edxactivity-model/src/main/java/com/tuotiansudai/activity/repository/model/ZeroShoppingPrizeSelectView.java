package com.tuotiansudai.activity.repository.model;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class ZeroShoppingPrizeSelectView implements Serializable{

    private String userName;
    private String mobile;
    private String investAmount;
    private String selectPrize;
    private Date investTime;

    public ZeroShoppingPrizeSelectView() {
    }

    public ZeroShoppingPrizeSelectView(ZeroShoppingPrizeSelectModel zeroShoppingPrizeSelectModel) {
        this.userName = zeroShoppingPrizeSelectModel.getUserName();
        this.mobile = zeroShoppingPrizeSelectModel.getMobile();
        this.investAmount = AmountConverter.convertCentToString(zeroShoppingPrizeSelectModel.getInvestAmount());
        this.selectPrize = zeroShoppingPrizeSelectModel.getSelectPrize().getDescription();
        this.investTime = zeroShoppingPrizeSelectModel.getInvestTime();
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

    public String getSelectPrize() {
        return selectPrize;
    }

    public void setSelectPrize(String selectPrize) {
        this.selectPrize = selectPrize;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }
}
