package com.tuotiansudai.repository.model;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class BankAccountModel implements Serializable {
    private long id;
    private String loginName;
    private String bankUserName;
    private String bankAccountNo;
    private String bankAccountOrderNo;
    private String bankAccountOrderDate;
    private long balance;
    private long membershipPoint;
    private boolean authorization;
    private boolean autoInvest;
    private long authorizationAmount;
    private Date authorizationEndTime;
    private String bankAuthorizationOrderNo;
    private String bankAuthorizationOrderDate;
    private Date createdTime;
    private Date updatedTime;

    public BankAccountModel() {
    }

    public BankAccountModel(String loginName, String bankUserName, String bankAccountNo, String bankAccountOrderNo, String bankAccountOrderDate) {
        this.loginName = loginName;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankAccountOrderNo = bankAccountOrderNo;
        this.bankAccountOrderDate = bankAccountOrderDate;
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

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankAccountOrderNo() {
        return bankAccountOrderNo;
    }

    public void setBankAccountOrderNo(String bankAccountOrderNo) {
        this.bankAccountOrderNo = bankAccountOrderNo;
    }

    public String getBankAccountOrderDate() {
        return bankAccountOrderDate;
    }

    public void setBankAccountOrderDate(String bankAccountOrderDate) {
        this.bankAccountOrderDate = bankAccountOrderDate;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getMembershipPoint() {
        return membershipPoint;
    }

    public void setMembershipPoint(long membershipPoint) {
        this.membershipPoint = membershipPoint;
    }

    public boolean isAuthorization() {
        return authorization;
    }

    public void setAuthorization(boolean authorization) {
        this.authorization = authorization;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public String getBankAuthorizationOrderNo() {
        return bankAuthorizationOrderNo;
    }

    public void setBankAuthorizationOrderNo(String bankAuthorizationOrderNo) {
        this.bankAuthorizationOrderNo = bankAuthorizationOrderNo;
    }

    public String getBankAuthorizationOrderDate() {
        return bankAuthorizationOrderDate;
    }

    public void setBankAuthorizationOrderDate(String bankAuthorizationOrderDate) {
        this.bankAuthorizationOrderDate = bankAuthorizationOrderDate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public long getAuthorizationAmount() {
        return authorizationAmount;
    }

    public void setAuthorizationAmount(long authorizationAmount) {
        this.authorizationAmount = authorizationAmount;
    }

    public Date getAuthorizationEndTime() {
        return authorizationEndTime;
    }

    public void setAuthorizationEndTime(Date authorizationEndTime) {
        this.authorizationEndTime = authorizationEndTime;
    }
}
