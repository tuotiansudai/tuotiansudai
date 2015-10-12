package com.tuotiansudai.repository.model;

import java.util.Date;

public class SystemBillModel {

    private long id;

    private Date createdTime = new Date();

    private SystemBillOperationType Type;

    private long amount;

    private String detail;

    private String orderId;

    private SystemBillBusinessType businessType;

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

    public SystemBillOperationType getType() {
        return Type;
    }

    public void setType(SystemBillOperationType type) {
        Type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public SystemBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(SystemBillBusinessType businessType) {
        this.businessType = businessType;
    }
}
