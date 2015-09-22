package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.utils.AmountUtil;

import java.util.Date;

public class LoanRepayDataItemDto {
    private long loanRepayId;

    private long loanId;

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

    private String loanRepayStatus;

    private boolean isEnabled;

    public LoanRepayDataItemDto(LoanRepayModel loanRepayModel, boolean isEnabled) {
        this.loanRepayId = loanRepayModel.getId();
        this.loanId = loanRepayModel.getLoanId();
        this.period = loanRepayModel.getPeriod();
        this.corpus = AmountUtil.convertCentToString(loanRepayModel.getCorpus());
        this.expectedInterest = AmountUtil.convertCentToString(loanRepayModel.getExpectedInterest());
        this.defaultInterest = AmountUtil.convertCentToString(loanRepayModel.getDefaultInterest());
        this.expectedRepayAmount = AmountUtil.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getExpectedInterest());
        this.repayDate = loanRepayModel.getRepayDate();
        this.actualInterest = AmountUtil.convertCentToString(loanRepayModel.getActualInterest());
        if (loanRepayModel.getStatus() == RepayStatus.COMPLETE) {
            this.actualRepayAmount = AmountUtil.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getActualInterest() + loanRepayModel.getDefaultInterest());
        }
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

    public String getLoanRepayStatus() {
        return loanRepayStatus;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public String getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public String getActualRepayAmount() {
        return actualRepayAmount;
    }
}
