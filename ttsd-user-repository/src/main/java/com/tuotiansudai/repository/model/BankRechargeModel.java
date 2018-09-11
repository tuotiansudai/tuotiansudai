package com.tuotiansudai.repository.model;


import com.tuotiansudai.enums.RechargeStatus;
import com.tuotiansudai.enums.Role;

import java.util.Date;

public class BankRechargeModel {

    private long id;

    private String loginName;

    private long amount;

    private long fee;

    private String payType;

    private String bankOrderNo;

    private String bankOrderDate;

    private RechargeStatus status;

    private Source source;

    private String channel;

    private Date updatedTime;

    private Date createdTime;

    private Role roleType;

    public BankRechargeModel() {
    }

    public BankRechargeModel(String loginName, long amount, String payType, Source source, String channel) {
        this.loginName = loginName;
        this.amount = amount;
        this.fee = 0;
        this.source = source;
        this.channel = channel;
        this.payType = payType;
        this.status = RechargeStatus.WAIT_PAY;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public RechargeStatus getStatus() {
        return status;
    }

    public void setStatus(RechargeStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Role getRoleType() {
        return roleType;
    }

    public void setRoleType(Role roleType) {
        this.roleType = roleType;
    }
}


