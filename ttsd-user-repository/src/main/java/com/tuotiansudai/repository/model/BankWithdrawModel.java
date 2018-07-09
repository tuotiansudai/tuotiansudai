package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;

import java.io.Serializable;
import java.util.Date;

public class BankWithdrawModel implements Serializable {

    private long id;

    private String loginName;

    private long amount;

    private long fee;

    private Source source;

    private String bankOrderNo;

    private String bankOrderDate;

    private WithdrawStatus status;

    private Date createdTime;

    private Date updatedTime;

    private Role roleType;

    public BankWithdrawModel() {
    }

    public BankWithdrawModel(String loginName, long amount, long fee, Source source) {
        this.loginName = loginName;
        this.amount = amount;
        this.fee = fee;
        this.status = WithdrawStatus.WAIT_PAY;
        this.source = source;
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
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

    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
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

    public Role getRoleType() {
        return roleType;
    }

    public void setRoleType(Role roleType) {
        this.roleType = roleType;
    }
}
