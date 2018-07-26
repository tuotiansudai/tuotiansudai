package com.tuotiansudai.fudian.umpdto;


import com.tuotiansudai.fudian.dto.BankDtoValidator;

import java.io.Serializable;

public class UmpRepayFeeDto implements Serializable, BankDtoValidator {

    private long loanId;

    private long loanRepayId;

    private long fee;

    public UmpRepayFeeDto() {
    }

    public UmpRepayFeeDto(long loanId, long loanRepayId, long fee) {
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.fee = fee;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getFee() {
        return fee;
    }

    @Override
    public boolean isValid() {
        return loanId > 0
                && loanRepayId > 0
                && fee >= 0;
    }
}
