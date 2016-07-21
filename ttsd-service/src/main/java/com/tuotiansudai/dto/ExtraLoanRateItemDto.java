package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExtraLoanRateItemDto implements Serializable {
    private String rate;
    private String amountLower;
    private String amountUpper;

    public ExtraLoanRateItemDto(ExtraLoanRateModel model) {
        this.rate = new BigDecimal(model.getRate()).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_DOWN).toString();
        this.amountLower = AmountConverter.convertCentToString(model.getMinInvestAmount());
        this.amountUpper = AmountConverter.convertCentToString(model.getMaxInvestAmount());
    }

    public String getRate() {
        return rate;
    }

    public String getAmountLower() {
        return amountLower;
    }

    public String getAmountUpper() {
        return amountUpper;
    }
}
