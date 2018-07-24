package com.tuotiansudai.fudian.umpmessage;


import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpWithdrawMessage extends BankBaseMessage{

    private long withdrawId;

    private String loginName;

    private long amount;

    private boolean isApply;

    private String applyMessage;

    private String notifyMessage;

    public UmpWithdrawMessage() {
    }

    public UmpWithdrawMessage(long withdrawId, String loginName, long amount) {
        this.withdrawId = withdrawId;
        this.loginName = loginName;
        this.amount = amount;
    }

    public long getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(long withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }

    public String getApplyMessage() {
        return applyMessage;
    }

    public void setApplyMessage(String applyMessage) {
        this.applyMessage = applyMessage;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }
}
