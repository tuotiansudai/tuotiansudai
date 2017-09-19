package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class CreditLoanBillModel implements Serializable {

    private long id;

    private Long orderId;

    private long amount;

    private CreditLoanBillOperationType operationType;

    private CreditLoanBillBusinessType businessType;

    private String detail;

    private Date createdTime;

    private String mobile;

    public CreditLoanBillModel() {
    }

    public CreditLoanBillModel(Long orderId, long amount, CreditLoanBillOperationType operationType, CreditLoanBillBusinessType businessType, String detail, String mobile) {
        this.orderId = orderId;
        this.amount = amount;
        this.operationType = operationType;
        this.businessType = businessType;
        this.detail = detail;
        this.createdTime = new Date();
        this.mobile = mobile;
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

    public CreditLoanBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(CreditLoanBillOperationType operationType) {
        this.operationType = operationType;
    }

    public CreditLoanBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(CreditLoanBillBusinessType businessType) {
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
