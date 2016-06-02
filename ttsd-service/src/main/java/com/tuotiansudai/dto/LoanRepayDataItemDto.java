package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class LoanRepayDataItemDto {
    private long loanId;

    private String loanName;

    private long loanRepayId;

    private int period;

    private String corpus;

    private String expectedInterest;

    private String actualInterest;

    private String defaultInterest;

    private String expectedRepayAmount;

    private String actualRepayAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    private Date actualRepayDate;

    private RepayStatus loanRepayStatus;

    private boolean isEnabled;

    private String agentLoginName;

    private String totalAmount;


    public LoanRepayDataItemDto(LoanRepayModel loanRepayModel) {
        this.loanId = loanRepayModel.getLoanId();
        this.loanName = loanRepayModel.getLoan().getName();
        this.loanRepayId = loanRepayModel.getId();
        this.period = loanRepayModel.getPeriod();
        this.corpus = AmountConverter.convertCentToString(loanRepayModel.getCorpus());
        this.expectedInterest = AmountConverter.convertCentToString(loanRepayModel.getExpectedInterest());
        this.defaultInterest = AmountConverter.convertCentToString(loanRepayModel.getDefaultInterest());
        this.expectedRepayAmount = AmountConverter.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getExpectedInterest());
        this.repayDate = loanRepayModel.getRepayDate();
        this.actualRepayDate = loanRepayModel.getActualRepayDate();
        this.actualInterest = AmountConverter.convertCentToString(loanRepayModel.getActualInterest());
        if (loanRepayModel.getStatus() == RepayStatus.COMPLETE) {
            this.actualRepayAmount = AmountConverter.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getActualInterest() + loanRepayModel.getDefaultInterest());
        }
        this.loanRepayStatus = loanRepayModel.getStatus();
        this.totalAmount = AmountConverter.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getExpectedInterest() + loanRepayModel.getDefaultInterest());
        this.agentLoginName = loanRepayModel.getLoan().getAgentLoginName();
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getLoanId() {
        return loanId;
    }

    public int getPeriod() {
        return period;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public String getDefaultInterest() {
        return defaultInterest;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public void setDefaultInterest(String defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RepayStatus getLoanRepayStatus() {
        return loanRepayStatus;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setLoanRepayStatus(RepayStatus loanRepayStatus) {
        this.loanRepayStatus = loanRepayStatus;
    }

    public String getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public String getActualRepayAmount() {
        return actualRepayAmount;
    }
}
