package com.tuotiansudai.cfca.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinContractRequestModel implements Serializable{
    private long id;
    private long businessId;
    private long orderId;
    private String contractNo;
    private String contractType;
    private String txTime;
    private String batchNo;
    private String templateId;
    private String isSign;
    private String jsonData;
    private Date updatedTime;
    private Date createdTime;

    public AnxinContractRequestModel() {
    }

    public AnxinContractRequestModel(long businessId, long orderId, String contractNo, String contractType, String txTime, String batchNo, String templateId, String isSign, String jsonData, Date createdTime) {
        this.businessId = businessId;
        this.orderId = orderId;
        this.contractNo = contractNo;
        this.contractType = contractType;
        this.txTime = txTime;
        this.batchNo = batchNo;
        this.templateId = templateId;
        this.isSign = isSign;
        this.jsonData = jsonData;
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
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
}
