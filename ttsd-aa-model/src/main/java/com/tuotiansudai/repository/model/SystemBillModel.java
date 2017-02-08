package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class SystemBillModel implements Serializable {

    private long id;

    private Long orderId;

    private long amount;

    private SystemBillOperationType operationType;

    private SystemBillBusinessType businessType;

    private String detail;

    private Date createdTime;

    public SystemBillModel() {
    }

    public SystemBillModel(Long orderId, long amount, SystemBillOperationType operationType, SystemBillBusinessType businessType, String detail) {
        this.orderId = orderId;
        this.amount = amount;
        this.operationType = operationType;
        this.businessType = businessType;
        this.detail = detail;
        this.createdTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
