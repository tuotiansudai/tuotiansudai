package com.tuotiansudai.cfca.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinContractResponseModel implements Serializable {

    private long id;
    private long businessId;
    private String batchNo;
    private String contractNo;
    private String txTime;
    private String locale;
    private String retCode;
    private String retMessage;
    private Date createdTime;
    private Date updatedTime;

    public AnxinContractResponseModel() {
    }

    public AnxinContractResponseModel(long businessId, String batchNo, String retCode,String retMessage) {
        this.businessId = businessId;
        this.batchNo = batchNo;
        this.retMessage = retMessage;
        this.retCode = retCode;
    }

    public AnxinContractResponseModel(long businessId, String batchNo,String contractNo, String txTime, String locale, Date createdTime) {
        this.businessId = businessId;
        this.batchNo = batchNo;
        this.contractNo = contractNo;
        this.txTime = txTime;
        this.locale = locale;
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
