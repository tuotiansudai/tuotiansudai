package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class WeChatHelpModel implements Serializable {

    private long id;
    private long loanId;
    private long investId;
    private long investAmount;
    private long annualizedAmount;
    private String loginName;
    private String openId;
    private WeChatHelpType type;
    private int helpUserCount;
    private long reward;
    private Date startTime;
    private Date endTime;
    private boolean isSend;

    public WeChatHelpModel() {
    }

    public WeChatHelpModel(long loanId, long investId, long investAmount,long annualizedAmount, String loginName, String openId, WeChatHelpType type, Date startTime, Date endTime) {
        this.loanId = loanId;
        this.investId = investId;
        this.investAmount = investAmount;
        this.annualizedAmount = annualizedAmount;
        this.loginName = loginName;
        this.openId = openId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getAnnualizedAmount() {
        return annualizedAmount;
    }

    public void setAnnualizedAmount(long annualizedAmount) {
        this.annualizedAmount = annualizedAmount;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public WeChatHelpType getType() {
        return type;
    }

    public void setType(WeChatHelpType type) {
        this.type = type;
    }

    public int getHelpUserCount() {
        return helpUserCount;
    }

    public void setHelpUserCount(int helpUserCount) {
        this.helpUserCount = helpUserCount;
    }

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
