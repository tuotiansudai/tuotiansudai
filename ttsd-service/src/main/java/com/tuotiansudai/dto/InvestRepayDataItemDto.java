package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.*;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.utils.AmountUtil;

import java.util.Date;

public class InvestRepayDataItemDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    private String amount;

    private String corpus;

    private String expectedFee;

    private String actualAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actualRepayDate;

    private int period;

    private String expectedInterest;

    private String actualInterest;

    private String defaultInterest;

    private String actualFee;

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
        this.actualFee = AmountUtil.convertCentToString(model.getActualFee());
        this.actualInterest = AmountUtil.convertCentToString(model.getActualInterest());
        this.actualRepayDate = model.getActualRepayDate();
        this.corpus = AmountUtil.convertCentToString(model.getCorpus());
        this.createdTime = model.getCreatedTime();
        this.defaultInterest = AmountUtil.convertCentToString(model.getDefaultInterest());
        this.expectedFee = AmountUtil.convertCentToString(model.getExpectedFee());
        this.expectedInterest = AmountUtil.convertCentToString(model.getExpectedInterest());
        this.repayDate = model.getRepayDate();
        this.status = model.getStatus();
        this.id = model.getId();
        this.investId = model.getInvestId();
        this.period = model.getPeriod();
        this.amount = AmountUtil.convertCentToString(model.getCorpus() + model.getExpectedInterest() - model.getExpectedFee());
        this.actualAmount = AmountUtil.convertCentToString(model.getCorpus() + model.getActualInterest() + model.getDefaultInterest() - model.getActualFee());
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(String expectedFee) {
        this.expectedFee = expectedFee;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(String defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }
}
