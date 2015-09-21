package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanRepayModel;

import java.util.Date;

public class LoanRepayDataItemDto {
    private long loanRepayId;
    private long loanId;
    private int period;
    private long corpus;
    private long expectedInterest;
    private long actualInterest;
    private long defaultInterest;
    private Date repayDate;
    private Date actualRepayDate;
    private String loanRepayStatus;
    private boolean isEnabled;

    public LoanRepayDataItemDto(LoanRepayModel loanRepayModel, boolean isEnabled) {
        this.loanRepayId = loanRepayModel.getId();
        this.loanId = loanRepayModel.getLoanId();
        this.period = loanRepayModel.getPeriod();
        this.corpus = loanRepayModel.getCorpus();
        this.expectedInterest = loanRepayModel.getExpectedInterest();
        this.defaultInterest = loanRepayModel.getDefaultInterest();
        this.repayDate = loanRepayModel.getRepayDate();
        this.actualInterest = loanRepayModel.getActualInterest();
        this.actualRepayDate = loanRepayModel.getActualRepayDate();
        this.loanRepayStatus = loanRepayModel.getStatus().getDescription();
        this.isEnabled = isEnabled;
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

    public long getCorpus() {
        return corpus;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public String getLoanRepayStatus() {
        return loanRepayStatus;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
