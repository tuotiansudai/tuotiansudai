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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actualRepayDate;

    private int period;

    private long expectedInterest;

    private long actualInterest;

    private long defaultInterest;

    private long actualFee;

    @JsonIgnore
    private long id;

    @JsonIgnore
    private long investId;

    @JsonIgnore
    private Date createdTime = new Date();

    private RepayStatus status;

    public InvestRepayDataItemDto() {
    }

    public InvestRepayDataItemDto(InvestRepayModel model) {
        this.actualFee = model.getActualFee();
        this.actualInterest = model.getActualInterest();
        this.actualRepayDate = model.getActualRepayDate();
        this.corpus = model.getCorpus();
        this.createdTime = model.getCreatedTime();
        this.defaultInterest = model.getDefaultInterest();
        this.expectedFee = model.getExpectedFee();
        this.expectedInterest = model.getExpectedInterest();
        this.repayDate = model.getRepayDate();
        this.status = model.getStatus();
        this.id = model.getId();
        this.investId = model.getInvestId();
        this.period = model.getPeriod();
        this.amount = this.corpus + this.expectedInterest - this.expectedFee;
        this.actualAmount = this.corpus + this.actualInterest + this.defaultInterest - this.actualFee;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCorpus() {
        return corpus;
    }

    public void setCorpus(long corpus) {
        this.corpus = corpus;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(long defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(long expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
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

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }

    public long getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(long expectedFee) {
        this.expectedFee = expectedFee;
    }

    public long getActualFee() {
        return actualFee;
    }

    public void setActualFee(long actualFee) {
        this.actualFee = actualFee;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(long actualAmount) {
        this.actualAmount = actualAmount;
    }
}
