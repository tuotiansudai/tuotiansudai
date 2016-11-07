package com.tuotiansudai.cfca.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinContractResponseModel implements Serializable {

    private long id;
    private long loanId;
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

    public AnxinContractResponseModel(long loanId, String batchNo, String retCode,String retMessage) {
        this.loanId = loanId;
        this.batchNo = batchNo;
        this.retMessage = retMessage;
        this.retCode = retCode;
    }

    public AnxinContractResponseModel(long loanId, String batchNo,String contractNo, String txTime, String locale, String retCode, String retMessage, Date createdTime) {
        this.loanId = loanId;
        this.batchNo = batchNo;
        this.contractNo = contractNo;
        this.txTime = txTime;
        this.locale = locale;
        this.retCode = retCode;
        this.retMessage = retMessage;
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

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
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
