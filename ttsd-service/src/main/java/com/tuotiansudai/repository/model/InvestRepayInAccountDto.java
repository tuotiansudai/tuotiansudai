package com.tuotiansudai.repository.model;


import java.util.Date;

public class InvestRepayInAccountDto {

    private Date createTime;

    private String loanName;

    private long loanId;

    private Date repayDate;

    private long corpus;

    private long expectedInterest;

    private long defaultInterest;

    private long expectedFee;

    private long amount;

    private RepayStatus status;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public long getCorpus() {
        return corpus;
    }

    public void setCorpus(long corpus) {
        this.corpus = corpus;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(long expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public long getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(long defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public long getExpectedFee() {
        return expectedFee;
    }

    public void setExpectedFee(long expectedFee) {
        this.expectedFee = expectedFee;
    }
}
