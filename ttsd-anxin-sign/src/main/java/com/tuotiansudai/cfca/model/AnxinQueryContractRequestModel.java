package com.tuotiansudai.cfca.model;

import com.tuotiansudai.cfca.dto.AnxinContractType;

import java.util.Date;

public class AnxinQueryContractRequestModel {

    private long id;
    private String batchNo;
    private AnxinContractType contractType;
    private String txTime;
    private String jsonDate;
    private Date createdTime;

    public AnxinQueryContractRequestModel() {
    }

    public AnxinQueryContractRequestModel(String batchNo, AnxinContractType contractType, String txTime, String jsonDate, Date createdTime) {
        this.batchNo = batchNo;
        this.contractType = contractType;
        this.txTime = txTime;
        this.jsonDate = jsonDate;
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

    public AnxinContractType getContractType() {
        return contractType;
    }

    public void setContractType(AnxinContractType contractType) {
        this.contractType = contractType;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getJsonDate() {
        return jsonDate;
    }

    public void setJsonDate(String jsonDate) {
        this.jsonDate = jsonDate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
