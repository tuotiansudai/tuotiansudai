package com.tuotiansudai.paywrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;

import java.io.Serializable;

public class InvestRepayJobResultDto implements Serializable {

    @JsonProperty(value = "investId")
    private long investId;

    @JsonProperty(value = "investRepayId")
    private long investRepayId;

    @JsonProperty(value = "investorLoginName")
    private String investorLoginName;

    @JsonProperty(value = "corpus")
    private long corpus;

    @JsonProperty(value = "actualInterest")
    private long actualInterest;

    @JsonProperty(value = "actualFee")
    private long actualFee;

    @JsonProperty(value = "defaultInterest")
    private long defaultInterest;

    @JsonProperty(value = "interestStatus")
    private SyncRequestStatus interestStatus;

    @JsonProperty(value = "isUpdateInvestorUserBillSuccess")
    private boolean isUpdateInvestorUserBillSuccess;

    @JsonProperty(value = "isUpdateInvestRepaySuccess")
    private boolean isUpdateInvestRepaySuccess;

    @JsonProperty(value = "feeStatus")
    private SyncRequestStatus feeStatus;

    @JsonProperty(value = "isUpdateSystemBillSuccess")
    private boolean isUpdateSystemBillSuccess;

    public InvestRepayJobResultDto() {
    }

    public InvestRepayJobResultDto(long investId, long investRepayId, String investorLoginName, long corpus, long actualInterest, long actualFee, long defaultInterest) {
        this.investId = investId;
        this.investRepayId = investRepayId;
        this.investorLoginName = investorLoginName;
        this.corpus = corpus;
        this.actualInterest = actualInterest;
        this.actualFee = actualFee;
        this.defaultInterest = defaultInterest;
        this.interestStatus = corpus + actualInterest + defaultInterest - actualFee == 0 ? SyncRequestStatus.SUCCESS : SyncRequestStatus.READY;
        this.feeStatus = actualFee == 0 ? SyncRequestStatus.SUCCESS : SyncRequestStatus.READY;
    }

    public long getInvestId() {
        return investId;
    }

    public long getInvestRepayId() {
        return investRepayId;
    }

    public String getInvestorLoginName() {
        return investorLoginName;
    }

    public long getCorpus() {
        return corpus;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public long getActualFee() {
        return actualFee;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public SyncRequestStatus getInterestStatus() {
        return interestStatus;
    }

    public boolean isUpdateInvestorUserBillSuccess() {
        return isUpdateInvestorUserBillSuccess;
    }

    public boolean isUpdateInvestRepaySuccess() {
        return isUpdateInvestRepaySuccess;
    }

    public SyncRequestStatus getFeeStatus() {
        return feeStatus;
    }

    public boolean isUpdateSystemBillSuccess() {
        return isUpdateSystemBillSuccess;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public void setInvestRepayId(long investRepayId) {
        this.investRepayId = investRepayId;
    }

    public void setInvestorLoginName(String investorLoginName) {
        this.investorLoginName = investorLoginName;
    }

    public void setCorpus(long corpus) {
        this.corpus = corpus;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public void setActualFee(long actualFee) {
        this.actualFee = actualFee;
    }

    public void setDefaultInterest(long defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public void setInterestStatus(SyncRequestStatus interestStatus) {
        this.interestStatus = interestStatus;
    }

    public void setUpdateInvestorUserBillSuccess(boolean updateInvestorUserBillSuccess) {
        isUpdateInvestorUserBillSuccess = updateInvestorUserBillSuccess;
    }

    public void setUpdateInvestRepaySuccess(boolean updateInvestRepaySuccess) {
        isUpdateInvestRepaySuccess = updateInvestRepaySuccess;
    }

    public void setFeeStatus(SyncRequestStatus feeStatus) {
        this.feeStatus = feeStatus;
    }

    public void setUpdateSystemBillSuccess(boolean updateSystemBillSuccess) {
        isUpdateSystemBillSuccess = updateSystemBillSuccess;
    }
}