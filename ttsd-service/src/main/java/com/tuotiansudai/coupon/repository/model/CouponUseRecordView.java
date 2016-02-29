package com.tuotiansudai.coupon.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CouponUseRecordView implements Serializable {

    private static final long serialVersionUID = 6557972198633723582L;

    private long id;

    private CouponType couponType;

    private long couponAmount;

    private double rate;

    private List<ProductType> productTypeList;

    private long loanId;

    private String loanName;

    private long investAmount;

    private Date usedTime;

    private long expectedIncome;

    private long investLowerLimit;

    private long investUpperLimit;

    private String birthdayBenefit;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private boolean shared;

    private ProductType loanProductType;

    public CouponUseRecordView() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public long getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(long couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public long getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(long expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

    public long getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(long investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public long getInvestUpperLimit() {
        return investUpperLimit;
    }

    public void setInvestUpperLimit(long investUpperLimit) {
        this.investUpperLimit = investUpperLimit;
    }

    public String getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(String birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }

    public ProductType getLoanProductType() {
        return loanProductType;
    }

    public void setLoanProductType(ProductType loanProductType) {
        this.loanProductType = loanProductType;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
