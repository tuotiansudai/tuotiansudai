package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanModel;

import java.io.Serializable;
import java.util.Date;

public class LoanerLoanPaginationItemDataDto implements Serializable {
    private long loanId;
    private String loanName;
    private long loanAmount;
    private long expectedRepayAmount;
    private long actualRepayAmount;
    private long unpaidAmount;
    private Date recheckTime;
    private Date nextRepayDate;
    private Date completedDate;


    public LoanerLoanPaginationItemDataDto(LoanModel loanModel) {
        this.loanId = loanModel.getId();
        this.loanName = loanModel.getName();
        this.loanAmount = loanModel.getLoanAmount();
        this.expectedRepayAmount = loanModel.getExpectedRepayAmount();
        this.actualRepayAmount = loanModel.getActualRepayAmount();
        this.unpaidAmount = loanModel.getUnpaidAmount();
        this.recheckTime = loanModel.getRecheckTime();
        this.nextRepayDate = loanModel.getNextRepayDate();
        this.completedDate = loanModel.getCompletedDate();
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public long getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public long getActualRepayAmount() {
        return actualRepayAmount;
    }

    public long getUnpaidAmount() {
        return unpaidAmount;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }
}
