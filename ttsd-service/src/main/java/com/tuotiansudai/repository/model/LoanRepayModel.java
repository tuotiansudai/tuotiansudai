package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class LoanRepayModel implements Serializable {

    private long id;

    private long loanId;

    private int period;

    private long corpus;

    private long expectedInterest;

    private long actualInterest;

    private long defaultInterest;

    private Date repayDate;

    private Date actualRepayDate;

    private RepayStatus status;

    private Date createdTime = new Date();

    public LoanRepayModel() {
    }

    public LoanRepayModel(long id, long loanId, int period, long expectedInterest, Date repayDate, RepayStatus status) {
        this.id = id;
        this.loanId = loanId;
        this.period = period;
        this.expectedInterest = expectedInterest;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public LoanModel getLoan() {
        return loan;
    }

    public void setLoan(LoanModel loan) {
        this.loan = loan;
    }
}
