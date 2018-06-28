package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankLoanCancelDto {

    private long loanId;

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    public BankLoanCancelDto() {
    }

    public BankLoanCancelDto(long loanId, String loanTxNo, String loanOrderNo, String loanOrderDate) {
        this.loanId = loanId;
        this.loanOrderNo = loanOrderNo;
        this.loanOrderDate = loanOrderDate;
        this.loanTxNo = loanTxNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public String getLoanOrderDate() {
        return loanOrderDate;
    }

    public boolean isValid() {
        return !Strings.isNullOrEmpty(loanOrderNo)
                && !Strings.isNullOrEmpty(loanOrderDate)
                && !Strings.isNullOrEmpty(loanTxNo);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
