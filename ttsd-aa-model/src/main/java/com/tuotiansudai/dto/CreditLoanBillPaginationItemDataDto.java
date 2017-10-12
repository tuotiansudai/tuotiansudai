package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class CreditLoanBillPaginationItemDataDto implements Serializable {

    private long id;

    private String orderId;

    private String amount;

    private String operationType;

    private String businessType;

    private Date createdTime;

    public CreditLoanBillPaginationItemDataDto(CreditLoanBillModel creditLoanBillModel) {
        this.id = creditLoanBillModel.getId();
        this.orderId = String.valueOf(creditLoanBillModel.getOrderId());
        this.amount = AmountConverter.convertCentToString(creditLoanBillModel.getAmount());
        this.operationType = creditLoanBillModel.getOperationType().getDescription();
        this.businessType = creditLoanBillModel.getBusinessType().getDescription();
        this.createdTime = creditLoanBillModel.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
