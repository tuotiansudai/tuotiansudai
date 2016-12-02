package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.LoanStatus;
import io.swagger.annotations.ApiModelProperty;

public class LoanListRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "标的类型", example = "SYL")
    private String productType;

    @ApiModelProperty(value = "标的状态", example = "RAISING")
    private LoanStatus loanStatus;

    @ApiModelProperty(value = "最小利率", example = "1")
    private double rateLower;

    @ApiModelProperty(value = "最大利率", example = "10")
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
