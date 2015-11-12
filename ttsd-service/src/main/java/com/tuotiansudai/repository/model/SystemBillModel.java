package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class SystemBillModel implements Serializable {

    private long id;

    private long amount;

    private String orderId;

    private SystemBillOperationType Type;

    private SystemBillBusinessType businessType;

    private String detail;

    private Date createdTime = new Date();

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public SystemBillOperationType getType() {
        return Type;
    }

    public void setType(SystemBillOperationType type) {
        Type = type;
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
