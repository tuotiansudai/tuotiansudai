package com.tuotiansudai.cfca.model;


import java.io.Serializable;
import java.util.Date;

public class AnxinContractRequestModel implements Serializable{
    private long id;
    private long loanId;
    private long investId;
    private String contractNo;
    private long agentSignId;
    private long investorSignId;
    private String txTime;
    private String batchNo;
    private String templateId;
    private String isSign;
    private String agentMobile;
    private String loanerIdentityNumber;
    private String recheckTime;
    private String totalRate;
    private String investorMobile;
    private String agentIdentityNumber;
    private String periods;
    private String pledge;
    private String endTime;
    private String investorIdentityNumber;
    private String loanerUserName;
    private String loanAmount;
    private Date createdTime;

    public AnxinContractRequestModel() {
    }

    public AnxinContractRequestModel(long loanId,long investId,long agentSignId, long investorSignId, String txTime, String batchNo, String templateId, String isSign, String agentMobile, String loanerIdentityNumber, String recheckTime, String totalRate, String investorMobile, String agentIdentityNumber, String periods, String pledge, String endTime, String investorIdentityNumber, String loanerUserName, String loanAmount, Date createdTime) {
        this.loanId = loanId;
        this.investId = investId;
        this.agentSignId = agentSignId;
        this.investorSignId = investorSignId;
        this.txTime = txTime;
        this.batchNo = batchNo;
        this.templateId = templateId;
        this.isSign = isSign;
        this.agentMobile = agentMobile;
        this.loanerIdentityNumber = loanerIdentityNumber;
        this.recheckTime = recheckTime;
        this.totalRate = totalRate;
        this.investorMobile = investorMobile;
        this.agentIdentityNumber = agentIdentityNumber;
        this.periods = periods;
        this.pledge = pledge;
        this.endTime = endTime;
        this.investorIdentityNumber = investorIdentityNumber;
        this.loanerUserName = loanerUserName;
        this.loanAmount = loanAmount;
        this.createdTime = createdTime;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
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

    public long getAgentSignId() {
        return agentSignId;
    }

    public void setAgentSignId(long agentSignId) {
        this.agentSignId = agentSignId;
    }

    public long getInvestorSignId() {
        return investorSignId;
    }

    public void setInvestorSignId(long investorSignId) {
        this.investorSignId = investorSignId;
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

    public String getAgentMobile() {
        return agentMobile;
    }

    public void setAgentMobile(String agentMobile) {
        this.agentMobile = agentMobile;
    }

    public String getLoanerIdentityNumber() {
        return loanerIdentityNumber;
    }

    public void setLoanerIdentityNumber(String loanerIdentityNumber) {
        this.loanerIdentityNumber = loanerIdentityNumber;
    }

    public String getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(String recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getInvestorMobile() {
        return investorMobile;
    }

    public void setInvestorMobile(String investorMobile) {
        this.investorMobile = investorMobile;
    }

    public String getAgentIdentityNumber() {
        return agentIdentityNumber;
    }

    public void setAgentIdentityNumber(String agentIdentityNumber) {
        this.agentIdentityNumber = agentIdentityNumber;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getPledge() {
        return pledge;
    }

    public void setPledge(String pledge) {
        this.pledge = pledge;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInvestorIdentityNumber() {
        return investorIdentityNumber;
    }

    public void setInvestorIdentityNumber(String investorIdentityNumber) {
        this.investorIdentityNumber = investorIdentityNumber;
    }

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerUserName) {
        this.loanerUserName = loanerUserName;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
