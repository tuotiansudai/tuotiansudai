package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.LoanStatus;

public class LoanListRequestDto extends BaseParamDto {
    private Integer index;
    private Integer pageSize;
    private String productType;
    private LoanStatus loanStatus;
    private double rateLower;
    private double rateUpper;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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
