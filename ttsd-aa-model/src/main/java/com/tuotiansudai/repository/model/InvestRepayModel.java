package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.text.MessageFormat;
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
    //逾期罚息利息
    private long overdueInterest;
    //逾期手续费
    private long overdueFee;

    @Override
    public String toString() {
        return MessageFormat.format("id:{0} investId:{1} period:{2} corpus:{3} expectedInterest:{4} actualInterest:{5} " +
                        "defaultInterest:{6} expectedFee:{7} actualFee:{8} repayAmount:{9} repayDate:{10} actualRepayDate:{11} " +
                        "status: {12} isTransferred:{13} createdTime:{14} transferStatus:{15} overdueInterest:{16} overdueFee:{17}", this.id, this.investId,
                this.period, this.corpus, this.expectedInterest, this.actualInterest, this.defaultInterest, this.expectedFee,
                this.actualFee, this.repayAmount, this.repayDate, this.actualRepayDate, this.status, this.isTransferred,
                this.createdTime, this.transferStatus,this.overdueInterest,this.overdueFee);
    }

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

    public long getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(long overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public long getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(long overdueFee) {
        this.overdueFee = overdueFee;
    }
}