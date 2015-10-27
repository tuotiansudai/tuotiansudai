package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.utils.AmountUtil;

import java.util.Date;

public class InvestPaginationItemDto {

    private String loginName;

    private String amount;

    private InvestSource source;

    private String expectedInterest;

    private Date createdTime;

    private boolean autoInvest;

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

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }
}
