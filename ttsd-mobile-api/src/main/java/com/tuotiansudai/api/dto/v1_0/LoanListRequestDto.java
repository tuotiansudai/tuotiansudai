package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.LoanStatus;
import io.swagger.annotations.ApiModelProperty;

public class LoanListRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private String productType;

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private LoanStatus loanStatus;

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private double rateLower;

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private double rateUpper;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public double getRateLower() {
        return rateLower;
    }

    public void setRateLower(double rateLower) {
        this.rateLower = rateLower;
    }

    public double getRateUpper() {
        return rateUpper;
    }

    public void setRateUpper(double rateUpper) {
        this.rateUpper = rateUpper;
    }
}
