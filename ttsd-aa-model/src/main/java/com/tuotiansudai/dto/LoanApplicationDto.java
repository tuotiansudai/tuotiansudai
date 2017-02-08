package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanApplicationRegion;
import com.tuotiansudai.repository.model.PledgeType;

public class LoanApplicationDto {
    private String loginName;
    private LoanApplicationRegion region;
    private int amount;
    private int period;
    private PledgeType pledgeType;
    private String pledgeInfo;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public LoanApplicationRegion getRegion() {
        return region;
    }

    public void setRegion(LoanApplicationRegion region) {
        this.region = region;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getPledgeInfo() {
        return pledgeInfo;
    }

    public void setPledgeInfo(String pledgeInfo) {
        this.pledgeInfo = pledgeInfo;
    }
}
