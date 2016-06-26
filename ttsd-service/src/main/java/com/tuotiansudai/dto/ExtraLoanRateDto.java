package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ExtraLoanRateDto implements Serializable{
    private double rate;
    private long amountLower;
    private long amountUpper;

    public ExtraLoanRateDto(ExtraLoanRateModel model) {
        this.rate = model.getRate();
        this.amountLower=model.getMinInvestAmount();
        this.amountUpper=model.getMaxInvestAmount();
    }

    public String getRate() {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        return decimalFormat.format(rate * 100);
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getAmountLower() {
        return AmountConverter.convertCentToString(amountLower);
    }

    public void setAmountLower(long amountLower) {
        this.amountLower = amountLower;
    }

    public String getAmountUpper() {
        return AmountConverter.convertCentToString(amountUpper);
    }

    public void setAmountUpper(long amountUpper) {
        this.amountUpper = amountUpper;
    }
}
