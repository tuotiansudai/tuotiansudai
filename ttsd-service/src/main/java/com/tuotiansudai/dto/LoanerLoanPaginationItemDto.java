package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanModel;

import java.util.Date;

public class LoanerLoanPaginationItemDto {
    private long loanId;
    private String loanName;
    private long loanAmount;
    private long expectedRepayAmount;
    private long actualRepayAmount;
    private long unpaidAmount;
    private Date recheckTime;
    private Date nextRepayDate;
    private Date completedRepayDate;


    public LoanerLoanPaginationItemDto(LoanModel loanModel) {
        this.loanId = loanModel.getId();
        this.loanName = loanModel.getName();
        this.loanAmount = loanModel.getLoanAmount();
        this.unpaidAmount = loanModel.getUnpaidAmount();
        this.nextRepayDate = loanModel.getNextRepayDate();

    }
}
