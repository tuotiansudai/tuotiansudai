package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.LoanStatus;

public class LoanListRequestDto extends BaseParamDto {
    private String productType;
    private LoanStatus loanStatus;
    private double rateLower;
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
