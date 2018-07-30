package com.tuotiansudai.fudian.umpmessage;

import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpExtraRepayMessage extends BankBaseMessage {

    private String loginName;

    private long investExtraRateId;

    private long interest;

    private long fee;

    public UmpExtraRepayMessage() {
    }

    public UmpExtraRepayMessage(String loginName, long investExtraRateId, long interest, long fee) {
        this.loginName = loginName;
        this.investExtraRateId = investExtraRateId;
        this.interest = interest;
        this.fee = fee;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getInvestExtraRateId() {
        return investExtraRateId;
    }

    public long getInterest() {
        return interest;
    }

    public long getFee() {
        return fee;
    }
}
