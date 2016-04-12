package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class LoanPaginationItemDataDto implements Serializable {

    private long loanId;

    private String loanName;

    private String loanAmount;

    private String expectedRepayAmount;

    private String actualRepayAmount;

    private String unpaidAmount;

    private Date recheckTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextRepayDate;

    private Date completedDate;


    public LoanPaginationItemDataDto(LoanModel loanModel) {

        this.loanId = loanModel.getId();
        this.loanName = loanModel.getName();
        this.loanAmount = AmountConverter.convertCentToString(loanModel.getLoanAmount());
        this.expectedRepayAmount = AmountConverter.convertCentToString(loanModel.getExpectedRepayAmount());
        this.actualRepayAmount = AmountConverter.convertCentToString(loanModel.getActualRepayAmount());
        this.unpaidAmount = AmountConverter.convertCentToString(loanModel.getUnpaidAmount());
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

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getExpectedRepayAmount() {
        return expectedRepayAmount;
    }

    public String getActualRepayAmount() {
        return actualRepayAmount;
    }

    public String getUnpaidAmount() {
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
