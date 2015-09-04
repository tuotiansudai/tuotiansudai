package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestSource;

import java.util.Date;

public class InvestRecordDataDto extends BaseRecordDataDto{
    private String loginName;
    private double amount;
    private InvestSource source;
    private double expectedRate;
    private Date createdTime;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public InvestSource getSource() {
        return source;
    }

    public void setSource(InvestSource source) {
        this.source = source;
    }

    public double getExpectedRate() {
        return expectedRate;
    }

    public void setExpectedRate(double expectedRate) {
        this.expectedRate = expectedRate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
