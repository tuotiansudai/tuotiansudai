package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.util.Date;

public class LoanerLoanRepayDataItemDto {

    private long loanId;

    private long loanRepayId;

    private int period;

    private String corpus;

    private String expectedInterest;

    private String expectedRepayAmount;

    private String defaultInterest;

    private String actualRepayAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    private Date actualRepayDate;

    private RepayStatus loanRepayStatus;

    private boolean isEnabled;

    public LoanerLoanRepayDataItemDto(LoanRepayModel loanRepayModel, boolean isEnabled) {
        this.loanId = loanRepayModel.getLoanId();
        this.loanRepayId = loanRepayModel.getId();
        this.period = loanRepayModel.getPeriod();
        this.corpus = AmountConverter.convertCentToString(loanRepayModel.getCorpus());
        this.expectedInterest = AmountConverter.convertCentToString(loanRepayModel.getExpectedInterest());
        this.defaultInterest = AmountConverter.convertCentToString(loanRepayModel.getDefaultInterest());
        this.expectedRepayAmount = AmountConverter.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getExpectedInterest());
        if (Lists.newArrayList(RepayStatus.WAIT_PAY, RepayStatus.COMPLETE).contains(loanRepayModel.getStatus())) {
            this.actualRepayAmount = AmountConverter.convertCentToString(loanRepayModel.getRepayAmount());
        }
        this.repayDate = loanRepayModel.getRepayDate();
        this.actualRepayDate = loanRepayModel.getActualRepayDate();
        this.loanRepayStatus = loanRepayModel.getStatus();
        this.isEnabled = isEnabled;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(String defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public String getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public void setExpectedRepayAmount(String expectedRepayAmount) {
        this.expectedRepayAmount = expectedRepayAmount;
    }

    public String getActualRepayAmount() {
        return actualRepayAmount;
    }

    public void setActualRepayAmount(String actualRepayAmount) {
        this.actualRepayAmount = actualRepayAmount;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public RepayStatus getLoanRepayStatus() {
        return loanRepayStatus;
    }

    public void setLoanRepayStatus(RepayStatus loanRepayStatus) {
        this.loanRepayStatus = loanRepayStatus;
    }

    @JsonProperty(value = "isEnabled")
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
