package com.tuotiansudai.console.repository.model;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;

public class UserMicroModelView implements Serializable {

    private long id;

    private String userName;

    private String mobile;

    private String channel;

    private Date registerTime;

    private int noInvestPeriod;

    private boolean invested;

    private long totalInvestAmount;

    private int investCount;

    private Double averageInvestAmount;

    private int loanCount;

    private Double averageLoanInvestAmount;

    private Integer transformPeriod = null;

    private Integer invest1st2ndTiming = null;

    private Integer invest1st3rdTiming = null;

    private Date lastInvestTime;

    private long totalRepayingAmount;

    private Date lastLoginTime;

    private Integer lastLoginToNow = null;

    private Source lastLoginSource;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public int getNoInvestPeriod() {
        return noInvestPeriod;
    }

    public void setNoInvestPeriod(int noInvestPeriod) {
        this.noInvestPeriod = noInvestPeriod;
    }

    public boolean isInvested() {
        return invested;
    }

    public void setInvested(boolean invested) {
        this.invested = invested;
    }

    public long getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(long totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public Double getAverageInvestAmount() {
        return averageInvestAmount;
    }

    public void setAverageInvestAmount(Double averageInvestAmount) {
        this.averageInvestAmount = averageInvestAmount;
    }

    public int getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(int loanCount) {
        this.loanCount = loanCount;
    }

    public Double getAverageLoanInvestAmount() {
        return averageLoanInvestAmount;
    }

    public void setAverageLoanInvestAmount(Double averageLoanInvestAmount) {
        this.averageLoanInvestAmount = averageLoanInvestAmount;
    }

    public Integer getTransformPeriod() {
        return transformPeriod;
    }

    public void setTransformPeriod(Integer transformPeriod) {
        this.transformPeriod = transformPeriod;
    }

    public Integer getInvest1st2ndTiming() {
        return invest1st2ndTiming;
    }

    public void setInvest1st2ndTiming(Integer invest1st2ndTiming) {
        this.invest1st2ndTiming = invest1st2ndTiming;
    }

    public Integer getInvest1st3rdTiming() {
        return invest1st3rdTiming;
    }

    public void setInvest1st3rdTiming(Integer invest1st3rdTiming) {
        this.invest1st3rdTiming = invest1st3rdTiming;
    }

    public Date getLastInvestTime() {
        return lastInvestTime;
    }

    public void setLastInvestTime(Date lastInvestTime) {
        this.lastInvestTime = lastInvestTime;
    }

    public long getTotalRepayingAmount() {
        return totalRepayingAmount;
    }

    public void setTotalRepayingAmount(long totalRepayingAmount) {
        this.totalRepayingAmount = totalRepayingAmount;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLastLoginToNow() {
        return lastLoginToNow;
    }

    public void setLastLoginToNow(Integer lastLoginToNow) {
        this.lastLoginToNow = lastLoginToNow;
    }

    public Source getLastLoginSource() {
        return lastLoginSource;
    }

    public void setLastLoginSource(Source lastLoginSource) {
        this.lastLoginSource = lastLoginSource;
    }
}
