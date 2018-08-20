package com.tuotiansudai.fudian.message;


import java.util.Date;

public class BankAuthorizationMessage {

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private String bankOrderNo;

    private String bankOrderDate;

    private boolean isOpen;

    private long amount;

    private Date endTime;

    public BankAuthorizationMessage() {
    }

    public BankAuthorizationMessage(String loginName, String mobile, String bankUserName, String bankAccountNo, String bankOrderNo, String bankOrderDate) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
