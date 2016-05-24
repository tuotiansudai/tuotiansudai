package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestRepayModel implements Serializable {

    private long id;

    private long investId;

    private int period;

    private long corpus;

    private long expectedInterest;

    private long actualInterest;

    private long defaultInterest;

    private long expectedFee;

    private long actualFee;

    private long repayAmount;

    private Date repayDate;

    private Date actualRepayDate;

    private RepayStatus status;

    private boolean isTransferred;

    private Date createdTime = new Date();

    private TransferStatus transferStatus;

    public InvestRepayModel() {
    }

    public InvestRepayModel(long id, long investId, int period, long corpus, long expectedInterest, long expectedFee, Date repayDate, RepayStatus status) {
        this.id = id;
        this.investId = investId;
        this.period = period;
        this.corpus = corpus;
        this.expectedInterest = expectedInterest;
        this.expectedFee = expectedFee;
        this.repayDate = repayDate;
        this.status = status;
    }

    public InvestRepayModel(long id, long investId, int period, long corpus, long expectedInterest, long expectedFee, Date repayDate, RepayStatus status, TransferStatus transferStatus) {
        this.id = id;
        this.investId = investId;
        this.period = period;
        this.corpus = corpus;
        this.expectedInterest = expectedInterest;
        this.expectedFee = expectedFee;
        this.repayDate = repayDate;
        this.status = status;
        this.transferStatus = transferStatus;
    }

    private LoanModel loan;

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

    public boolean isTransferred() {
        return isTransferred;
    }

    public void setTransferred(boolean transferred) {
        isTransferred = transferred;
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

    public long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public LoanModel getLoan() {
        return loan;
    }

    public void setLoan(LoanModel loan) {
        this.loan = loan;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }
}
