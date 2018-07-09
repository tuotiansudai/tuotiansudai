package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.enums.BankUserBillOperationType;
import com.tuotiansudai.enums.Role;

import java.io.Serializable;
import java.util.Date;

public class BankUserBillModel implements Serializable {

    private long id;

    private long businessId;

    private String loginName;

    private String mobile;

    private String userName;

    private Role role;

    private long amount;

    private long balance;

    private String bankOrderNo;

    private String bankOrderDate;

    private BankUserBillBusinessType businessType;

    private BankUserBillOperationType operationType;

    private Date createdTime;

    public BankUserBillModel() {
    }

    public BankUserBillModel(long businessId, String loginName, String mobile, String userName, Role role, long amount, long balance, String bankOrderNo, String bankOrderDate, BankUserBillBusinessType businessType, BankUserBillOperationType operationType) {
        this.businessId = businessId;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.role = role;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.amount = amount;
        this.balance = balance;
        this.businessType = businessType;
        this.operationType = operationType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public BankUserBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BankUserBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public BankUserBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(BankUserBillOperationType operationType) {
        this.operationType = operationType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
