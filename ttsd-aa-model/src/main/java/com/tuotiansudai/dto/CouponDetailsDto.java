package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.ProductType;

import java.util.Date;

public class CouponDetailsDto {

    private String loginName;

    private Date usedTime;

    private Date endTime;

    private Long investAmount;

    private Long loanId;

    private String loanName;

    private ProductType productType;

    private Long annualInterest;

    public CouponDetailsDto(String loginName, Date usedTime, Long investAmount, Long loanId, String loanName, ProductType productType, Long annualInterest, Date endTime) {
        this.loginName = loginName;
        this.usedTime = usedTime;
        this.investAmount = investAmount;
        this.loanId = loanId;
        this.loanName = loanName;
        this.productType = productType;
        this.annualInterest = annualInterest;
        this.endTime = endTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Long investAmount) {
        this.investAmount = investAmount;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Long getAnnualInterest() {
        return annualInterest;
    }

    public void setAnnualInterest(Long annualInterest) {
        this.annualInterest = annualInterest;
    }
}
