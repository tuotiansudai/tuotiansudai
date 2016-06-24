package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.ExtraLoanRateModel;

import java.io.Serializable;

public class ExtraLoanRateDto implements Serializable{
    private double rate;
    private long amountLower;
    private long amountUpper;

    public ExtraLoanRateDto(ExtraLoanRateModel extraLoanRateModel) {
        this.rate = extraLoanRateModel.getRate();
        this.amountLower = extraLoanRateModel.getMinInvestAmount();
        this.amountUpper = extraLoanRateModel.getMaxInvestAmount();
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getAmountLower() {
        return amountLower;
    }

    public void setAmountLower(long amountLower) {
        this.amountLower = amountLower;
    }

    public long getAmountUpper() {
        return amountUpper;
    }

    public void setAmountUpper(long amountUpper) {
        this.amountUpper = amountUpper;
    }
}
