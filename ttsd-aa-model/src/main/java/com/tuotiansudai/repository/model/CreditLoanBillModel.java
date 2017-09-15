package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class CreditLoanBillModel implements Serializable {

    private long id;

    private Long orderId;

    private long amount;

    private SystemBillOperationType operationType;

    private CreditLoanBillBusinessType businessType;

    private String detail;

    private Date createdTime;

    private String loginName;

    public CreditLoanBillModel() {
    }

    public CreditLoanBillModel(Long orderId, long amount, SystemBillOperationType operationType, CreditLoanBillBusinessType businessType, String detail, String loginName) {
        this.orderId = orderId;
        this.amount = amount;
        this.operationType = operationType;
        this.businessType = businessType;
        this.detail = detail;
        this.createdTime = new Date();
        this.loginName = loginName;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
