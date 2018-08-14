package com.tuotiansudai.cfca.model;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.request.tx3.Tx3202ReqVO;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;

import java.io.Serializable;
import java.util.Date;

public class AnxinCreateContractBatchRequestModel implements Serializable{
    private long id;
    private long businessId;
    private String batchNo;
    private String txTime;
    private long orderId;
    private String templateId;
    private int isSign;
    private String signInfos;
    private String investmentInfo;
    private Date createdTime;


    public AnxinCreateContractBatchRequestModel(long businessId, Tx3202ReqVO tx3202ReqVO, CreateContractVO createContractVO) {
        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        this.businessId = businessId;
        this.orderId =createContractVO.getInvestmentInfo().get("orderId")==null?0: Long.parseLong(createContractVO.getInvestmentInfo().get("orderId"));
        this.txTime = tx3202ReqVO.getHead().getTxTime();
        this.batchNo = tx3202ReqVO.getBatchNo();
        this.templateId = createContractVO.getTemplateId();
        this.isSign = createContractVO.getIsSign();
        this.signInfos = jsonObjectMapper.writeValueAsString(createContractVO.getSignInfos());
        this.investmentInfo = jsonObjectMapper.writeValueAsString(createContractVO.getInvestmentInfo());
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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
