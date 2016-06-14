package com.tuotiansudai.paywrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;

import java.io.Serializable;

public class InvestRepayJobResultDto implements Serializable {

    private long investId;

    private long investRepayId;

    private String investorLoginName;

    private long corpus;

    private long actualInterest;

    private long actualFee;

    private long defaultInterest;

    private SyncRequestStatus interestStatus;

    private boolean isUpdateInvestorUserBillSuccess;

    private boolean isUpdateInvestRepaySuccess;

    private SyncRequestStatus feeStatus;

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

    @JsonProperty(value = "investId")
    public long getInvestId() {
        return investId;
    }

    @JsonProperty(value = "investRepayId")
    public long getInvestRepayId() {
        return investRepayId;
    }

    @JsonProperty(value = "investorLoginName")
    public String getInvestorLoginName() {
        return investorLoginName;
    }

    @JsonProperty(value = "corpus")
    public long getCorpus() {
        return corpus;
    }

    @JsonProperty(value = "actualInterest")
    public long getActualInterest() {
        return actualInterest;
    }

    @JsonProperty(value = "actualFee")
    public long getActualFee() {
        return actualFee;
    }

    @JsonProperty(value = "defaultInterest")
    public long getDefaultInterest() {
        return defaultInterest;
    }

    @JsonProperty(value = "interestStatus")
    public SyncRequestStatus getInterestStatus() {
        return interestStatus;
    }

    @JsonProperty(value = "isUpdateInvestorUserBillSuccess")
    public boolean isUpdateInvestorUserBillSuccess() {
        return isUpdateInvestorUserBillSuccess;
    }

    @JsonProperty(value = "isUpdateInvestRepaySuccess")
    public boolean isUpdateInvestRepaySuccess() {
        return isUpdateInvestRepaySuccess;
    }

    @JsonProperty(value = "feeStatus")
    public SyncRequestStatus getFeeStatus() {
        return feeStatus;
    }

    @JsonProperty(value = "isUpdateSystemBillSuccess")
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