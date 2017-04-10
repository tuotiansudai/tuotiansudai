package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.riskestimation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RiskEstimateViewModel implements Serializable {
    private long id;

    private String loginName;

    private String mobile;

    private String userName;

    private Date registerTime;

    private long investingAmount;

    private Estimate estimate;

    private Income income;

    private Rate rate;

    private long expectedInvestAmount;

    private Duration duration;

    private Age age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public long getInvestingAmount() {
        return investingAmount;
    }

    public void setInvestingAmount(long investingAmount) {
        this.investingAmount = investingAmount;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public long getExpectedInvestAmount() {
        return expectedInvestAmount;
    }

    public void setExpectedInvestAmount(long expectedInvestAmount) {
        this.expectedInvestAmount = expectedInvestAmount;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }
}
