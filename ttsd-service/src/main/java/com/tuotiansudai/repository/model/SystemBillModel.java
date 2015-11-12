package com.tuotiansudai.repository.model;

import java.util.Date;

public class SystemBillModel {

    private long id;

    private Long orderId;

    private long amount;

    private SystemBillOperationType type;

    private SystemBillBusinessType businessType;

    private String detail;

    private Date createdTime;

    public SystemBillModel() {
    }

    public SystemBillModel(Long orderId, long amount, SystemBillOperationType type, SystemBillBusinessType businessType, String detail) {
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
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

    public SystemBillOperationType getType() {
        return type;
    }

    public void setType(SystemBillOperationType type) {
        this.type = type;
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
