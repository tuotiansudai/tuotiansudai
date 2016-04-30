package com.tuotiansudai.api.dto;


import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.LoanStatus;

public class LoanListRequestDto extends BaseParamDto {
    private ProductType productType;
    private LoanStatus loanStatus;
    private double rateLower;
    private double rateUpper;

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
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
