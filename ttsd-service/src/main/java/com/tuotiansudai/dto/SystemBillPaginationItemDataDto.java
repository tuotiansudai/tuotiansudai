package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.utils.AmountUtil;

import java.io.Serializable;
import java.util.Date;

public class SystemBillPaginationItemDataDto implements Serializable {

    private long id;

    private String orderId;

    private String amount;

    private String operationType;

    private String businessType;

    private Date createdTime;

    private String detail;


    public SystemBillPaginationItemDataDto(SystemBillModel systemBillModel) {
        this.id = systemBillModel.getId();
        this.orderId = systemBillModel.getOrderId();
        this.amount = AmountUtil.convertCentToString(systemBillModel.getAmount());
        this.operationType = systemBillModel.getType().getDescription();
        this.businessType = systemBillModel.getBusinessType().getDescription();
        this.detail = systemBillModel.getDetail();
        this.createdTime = systemBillModel.getCreatedTime();
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
