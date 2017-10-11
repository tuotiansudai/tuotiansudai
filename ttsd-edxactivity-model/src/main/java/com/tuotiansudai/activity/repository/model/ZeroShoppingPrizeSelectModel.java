package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;
import java.util.Date;

public class ZeroShoppingPrizeSelectModel implements Serializable{

    private long id;
    private String mobile;
    private String userName;
    private long investAmount;
    private ZeroShoppingPrize selectPrize;
    private Date investTime;


    public ZeroShoppingPrizeSelectModel(long id, String mobile, String userName, long investAmount, ZeroShoppingPrize prize, Date investTime) {
        this.id = id;
        this.mobile = mobile;
        this.userName = userName;
        this.investAmount = investAmount;
        this.selectPrize = prize;
        this.investTime = investTime;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public ZeroShoppingPrize getSelectPrize() {
        return selectPrize;
    }

    public void setSelectPrize(ZeroShoppingPrize selectPrize) {
        this.selectPrize = selectPrize;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }
}
