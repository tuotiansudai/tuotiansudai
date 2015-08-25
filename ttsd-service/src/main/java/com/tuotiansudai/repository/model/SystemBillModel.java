package com.tuotiansudai.repository.model;

import java.util.Date;

public class SystemBillModel {

    private long id;

    private Date createdTime = new Date();

    private SystemBillType billType;

    private long money;

    private String detail;

    private long balance;

    private String reason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public SystemBillType getBillType() {
        return billType;
    }

    public void setBillType(SystemBillType billType) {
        this.billType = billType;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
