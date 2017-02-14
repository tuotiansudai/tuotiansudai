package com.tuotiansudai.cfca.model;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;

import java.io.Serializable;
import java.util.Date;

public class AnxinCreateContractBatchResponseModel implements Serializable {

    private long id;
    private long businessId;
    private String batchNo;
    private String txTime;
    private String orderId;
    private String retCode;
    private String retMessage;
    private String contractNo;
    private String templateId;
    private int isSign;
    private String signInfos;
    private String investmentInfo;
    private Date createdTime;


    public AnxinCreateContractBatchResponseModel(long businessId, String batchNo, String retCode, String retMessage) {
        this.businessId = businessId;
        this.batchNo = batchNo;
        this.retMessage = retMessage;
        this.retCode = retCode;
        this.createdTime = new Date();
    }

    public AnxinCreateContractBatchResponseModel(long businessId, Tx3202ResVO tx3202ResVO, CreateContractVO contractVO) {
        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        this.businessId = businessId;
        this.orderId = contractVO.getInvestmentInfo().get("orderId");
        this.batchNo = tx3202ResVO.getBatchNo();
        this.txTime = tx3202ResVO.getHead().getTxTime();
        this.contractNo = contractVO.getContractNo();
        this.retCode = tx3202ResVO.getHead().getRetCode();
        this.retMessage = tx3202ResVO.getHead().getRetMessage();
        this.templateId = contractVO.getTemplateId();
        this.isSign = contractVO.getIsSign();
        this.signInfos = jsonObjectMapper.writeValueAsString(contractVO.getSignInfos());
        this.investmentInfo = jsonObjectMapper.writeValueAsString(contractVO.getInvestmentInfo());
        this.createdTime = new Date();
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public String getSignInfos() {
        return signInfos;
    }

    public void setSignInfos(String signInfos) {
        this.signInfos = signInfos;
    }

    public String getInvestmentInfo() {
        return investmentInfo;
    }

    public void setInvestmentInfo(String investmentInfo) {
        this.investmentInfo = investmentInfo;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
