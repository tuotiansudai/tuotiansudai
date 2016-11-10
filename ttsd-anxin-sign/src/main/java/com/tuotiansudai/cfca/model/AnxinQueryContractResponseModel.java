package com.tuotiansudai.cfca.model;

import com.tuotiansudai.cfca.dto.AnxinContractType;

import java.util.Date;


public class AnxinQueryContractResponseModel {

    private long id;
    private String batchNo;
    private String contractNo;
    private String txTime;
    private String retCode;
    private String retMessage;
    private String jsonData;
    private Date createdTime;

    public AnxinQueryContractResponseModel() {
    }

    public AnxinQueryContractResponseModel(String batchNo, String contractNo, String txTime, String retCode, String retMessage, String jsonDate, Date createdTime) {
        this.batchNo = batchNo;
        this.contractNo = contractNo;
        this.txTime = txTime;
        this.retCode = retCode;
        this.retMessage = retMessage;
        this.jsonData = jsonDate;
        this.createdTime = createdTime;
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
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

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
