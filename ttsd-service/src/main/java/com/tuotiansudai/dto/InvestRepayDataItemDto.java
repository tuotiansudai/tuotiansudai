package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;

import java.util.Date;

public class InvestRepayDataItemDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    private long amount;

    private long corpus;

    private long expectedFee;

    private long actualAmount;

    private Date actualRepayDate;

    private int period;

    private long expectedInterest;

    private long actualInterest;

    private long defaultInterest;

    private long actualFee;

    private String status;

    public InvestRepayDataItemDto() {
    }

    public InvestRepayDataItemDto(InvestRepayModel model) {
        this.actualFee = model.getActualFee();
        this.actualInterest = model.getActualInterest();
        this.actualRepayDate = model.getActualRepayDate();
        this.corpus = model.getCorpus();
        this.defaultInterest = model.getDefaultInterest();
        this.expectedFee = model.getExpectedFee();
        this.expectedInterest = model.getExpectedInterest();
        this.repayDate = model.getRepayDate();
        this.status = model.getStatus().getDescription();
        this.period = model.getPeriod();
        this.amount = this.corpus + this.expectedInterest - this.expectedFee;
        this.actualAmount = this.corpus + this.actualInterest + this.defaultInterest - this.actualFee;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public long getAmount() {
        return amount;
    }

    public long getCorpus() {
        return corpus;
    }

    public long getExpectedFee() {
        return expectedFee;
    }

    public long getActualAmount() {
        return actualAmount;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public int getPeriod() {
        return period;
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

    public long getActualFee() {
        return actualFee;
    }

    public String getStatus() {
        return status;
    }
}
