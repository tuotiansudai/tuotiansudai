package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.SystemBillBusinessType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qduljs2011 on 2018/7/18.
 */
public class BankSystemBillModel implements Serializable {
    private long id;

    private String bankOrderNo;

    private String bankOrderDate;

    private long amount;

    private SystemBillOperationType operationType;

    private SystemBillBusinessType businessType;

    private String detail;

    private Date createdTime;

    public BankSystemBillModel() {
    }

    public BankSystemBillModel(String bankOrderNo, String bankOrderDate, long amount, SystemBillOperationType operationType, SystemBillBusinessType businessType, String detail) {
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.amount = amount;
        this.operationType = operationType;
        this.businessType = businessType;
        this.detail = detail;
        this.createdTime=new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public SystemBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(SystemBillOperationType operationType) {
        this.operationType = operationType;
    }

    public SystemBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(SystemBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
