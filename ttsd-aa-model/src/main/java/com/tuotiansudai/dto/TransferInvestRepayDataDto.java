package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.RepayStatus;

import java.io.Serializable;
import java.util.Date;

public class TransferInvestRepayDataDto implements Serializable {
    private Date repayDate;

    private long expectedInterest;

    private RepayStatus status;

    private String statusDesc;

    public TransferInvestRepayDataDto() {
    }

    public TransferInvestRepayDataDto(Date repayDate, long expectedInterest, RepayStatus status) {
        this.repayDate = repayDate;
        this.expectedInterest = expectedInterest;
        this.status = status;
        this.statusDesc = status.getViewText();
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
