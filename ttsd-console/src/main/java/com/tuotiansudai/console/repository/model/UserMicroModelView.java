package com.tuotiansudai.console.repository.model;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;

public class UserMicroModelView implements Serializable {

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

    private int transformPeriod;

    private int invest1st2ndTiming;

    private int invest1st3rdTiming;

    private Date lastInvestTime;

    private long totalRepayingAmount;

    private Date lastLoginTime;

    private int lastLoginToNow;

    private Source lastLoginSource;

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

    public int getTransformPeriod() {
        return transformPeriod;
    }

    public void setTransformPeriod(int transformPeriod) {
        this.transformPeriod = transformPeriod;
    }

    public int getInvest1st2ndTiming() {
        return invest1st2ndTiming;
    }

    public void setInvest1st2ndTiming(int invest1st2ndTiming) {
        this.invest1st2ndTiming = invest1st2ndTiming;
    }

    public int getInvest1st3rdTiming() {
        return invest1st3rdTiming;
    }

    public void setInvest1st3rdTiming(int invest1st3rdTiming) {
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

    public int getLastLoginToNow() {
        return lastLoginToNow;
    }

    public void setLastLoginToNow(int lastLoginToNow) {
        this.lastLoginToNow = lastLoginToNow;
    }

    public Source getLastLoginSource() {
        return lastLoginSource;
    }

    public void setLastLoginSource(Source lastLoginSource) {
        this.lastLoginSource = lastLoginSource;
    }
}
