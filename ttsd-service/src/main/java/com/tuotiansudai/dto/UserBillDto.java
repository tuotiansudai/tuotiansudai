package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.UserBillBusinessType;

/**
 * Created by Administrator on 2015/9/11.
 */
public class UserBillDto {

    private String createdTime;

    private UserBillBusinessType businessType;

    private String income;

    private String expenditure;

    private String freeze;

    private String balance;

    private long id;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public UserBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(UserBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
