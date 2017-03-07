package com.tuotiansudai.cfca.model;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.response.tx3.Tx3211ResVO;
import com.tuotiansudai.cfca.converter.JsonObjectMapper;

import java.util.Date;

public class AnxinQueryContractBatchResponseModel {

    /**
     * common properties:
     */
    private long id;
    private long businessId;
    private String batchNo;
    private String txTime;
    private String retCode;
    private String retMessage;
    private Date createdTime;

    /**
     * contract properties:
     */
    private String templateId;
    private int isSign;
    private String contractNo;
    private String fileId;
    private String code;
    private String message;
    private String signInfos;
    private String investmentInfo;


    public AnxinQueryContractBatchResponseModel() {
    }

    public AnxinQueryContractBatchResponseModel(long businessId, Tx3211ResVO tx3211ResVO, CreateContractVO createContractVO) {
        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
        this.businessId = businessId;
        this.batchNo = tx3211ResVO.getBatchNo();
        this.txTime = tx3211ResVO.getHead().getTxTime();
        this.retCode = tx3211ResVO.getHead().getRetCode();
        this.retMessage = tx3211ResVO.getHead().getRetMessage();
        if (createContractVO != null) {
            this.templateId = createContractVO.getTemplateId();
            this.isSign = createContractVO.getIsSign();
            this.contractNo = createContractVO.getContractNo();
            this.fileId = createContractVO.getFileId();
            this.code = createContractVO.getCode();
            this.message = createContractVO.getMessage();
            this.signInfos = jsonObjectMapper.writeValueAsString(createContractVO.getSignInfos());
            this.investmentInfo = jsonObjectMapper.writeValueAsString(createContractVO.getInvestmentInfo());
        }
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
