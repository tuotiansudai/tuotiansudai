package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import com.tuotiansudai.util.AmountConverter;

public class ExtraLoanRateDto {
    private String minInvestAmount;

    private String maxInvestAmount;

    private double rate;

    public ExtraLoanRateDto(String minInvestAmount, String maxInvestAmount, double rate) {
        this.minInvestAmount = minInvestAmount;
        this.maxInvestAmount = maxInvestAmount;
        this.rate = rate;
    }

    public ExtraLoanRateDto(ExtraLoanRateModel extraLoanRateModel){
        this.minInvestAmount = AmountConverter.convertCentToString(extraLoanRateModel.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(extraLoanRateModel.getMaxInvestAmount());
        this.rate = extraLoanRateModel.getRate();
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
