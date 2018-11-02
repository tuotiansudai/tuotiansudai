package com.tuotiansudai.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class TransferableInvestView implements Serializable {
    private long investId;

    private long loanId;

    private String loanName;

    private long amount;

    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date nextRepayDate;

    private long nextRepayAmount;

    private TransferStatus transferStatus;

    private double baseRate;

    private double activityRate;

    private boolean isOverdueTransfer;

    public long getInvestId() {
        return investId;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }


    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public String getSumRatePercent(){
        return new DecimalFormat("######0.##").format((activityRate + baseRate) * 100);
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getNextRepayAmount() {
        return nextRepayAmount;
    }

    public void setNextRepayAmount(long nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }

    public boolean isOverdueTransfer() {
        return isOverdueTransfer;
    }

    public void setOverdueTransfer(boolean overdueTransfer) {
        isOverdueTransfer = overdueTransfer;
    }
}
