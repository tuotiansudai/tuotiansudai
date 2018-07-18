package com.tuotiansudai.fudian.umpmessage;


import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpRechargeMessage extends BankBaseMessage{

    private long rechargeId;

    private String loginName;

    private long amount;

    public UmpRechargeMessage() {
    }

    public UmpRechargeMessage(long rechargeId, String loginName, long amount) {
        this.rechargeId = rechargeId;
        this.loginName = loginName;
        this.amount = amount;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getAmount() {
        return amount;
    }
}
