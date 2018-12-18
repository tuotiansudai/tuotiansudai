package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.text.DecimalFormat;

public class ExtraLoanRateDto implements Serializable {

    @ApiModelProperty(value = "利率", example = "1")
    private double rate;

    @ApiModelProperty(value = "最少出借", example = "10")
    private long amountLower;

    @ApiModelProperty(value = "最大出借", example = "100")
    private long amountUpper;

    public ExtraLoanRateDto(ExtraLoanRateModel extraLoanRateModel) {
        this.rate = extraLoanRateModel.getRate();
        this.amountLower = extraLoanRateModel.getMinInvestAmount();
        this.amountUpper = extraLoanRateModel.getMaxInvestAmount();
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
