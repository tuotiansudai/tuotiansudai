package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestSource;

public class InvestRecordDto {
    private String loginName;
    private String amount;
    private InvestSource source;
    private String expectedRate;
    private String createdTime;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public InvestSource getSource() {
        return source;
    }

    public void setSource(InvestSource source) {
        this.source = source;
    }

    public String getExpectedRate() {
        return expectedRate;
    }

    public void setExpectedRate(String expectedRate) {
        this.expectedRate = expectedRate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
