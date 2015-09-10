package com.tuotiansudai.repository.model;

import java.util.Date;

/**
 * Created by Administrator on 2015/9/8.
 */
public class LoanRepayModel {

    private long id;

    private long corpus;

    private long defaultInterest;

    private long expectInterest;

    private long actualInterest;

    private long period;

    private Date repayDate;

    private RepayStatus status;

    private Date time;

    private long loanId;

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

    public long getExpectInterest() {
        return expectInterest;
    }

    public void setExpectInterest(long expectInterest) {
        this.expectInterest = expectInterest;
    }

    public long getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

}
