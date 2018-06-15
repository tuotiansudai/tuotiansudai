package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.UserBillBusinessType;

import java.io.Serializable;
import java.util.Date;

public class UserBillModel implements Serializable {

    private long id;

    private String loginName;

    private Long orderId;

    private String bankOrderNo;

    private String bankOrderDate;

    private long amount;

    private long balance;

    private long freeze;

    private UserBillBusinessType businessType;

    private UserBillOperationType operationType;

    private Date createdTime = new Date();

    public UserBillModel() {
    }

    public UserBillModel(String loginName, long orderId, String bankOrderNo, String bankOrderDate, long amount, long balance, UserBillBusinessType businessType, UserBillOperationType operationType) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.amount = amount;
        this.balance = balance;
        this.freeze = 0;
        this.businessType = businessType;
        this.operationType = operationType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public void setBankOrderDate(String bankOrderDate) {
        this.bankOrderDate = bankOrderDate;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getFreeze() {
        return freeze;
    }

    public void setFreeze(long freeze) {
        this.freeze = freeze;
    }

    public UserBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(UserBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public UserBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(UserBillOperationType operationType) {
        this.operationType = operationType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
