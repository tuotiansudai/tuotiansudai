package com.tuotiansudai.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class UserCouponResponseDataDto {

    private String userCouponId;

    private CouponType type;

    private String name;

    private String amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String investLowerLimit;

    private List<ProductType> productTypes;

    private String loanId;

    private String loanName;

    private ProductType loanProductType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date usedTime;

    private String expectedInterest;

    private String rate;

    private String investUpperLimit;

    private boolean shared;

    private String investAmount;

    private String birthdayRate;

    public UserCouponResponseDataDto() {
    }

    public UserCouponResponseDataDto(CouponModel couponModel, UserCouponModel userCouponModel) {
        super();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.userCouponId = String.valueOf(userCouponModel.getId());
        this.type = couponModel.getCouponType();
        this.name = couponModel.getCouponType().getName();
        this.amount = AmountConverter.convertCentToString(couponModel.getAmount());
        this.startDate = userCouponModel.getStartTime();
        this.endDate = userCouponModel.getEndTime();
        this.investLowerLimit = AmountConverter.convertCentToString(couponModel.getInvestLowerLimit());
        this.productTypes = couponModel.getProductTypes();
        this.usedTime = userCouponModel.getUsedTime();
        this.expectedInterest = AmountConverter.convertCentToString(userCouponModel.getExpectedInterest());
        this.rate = decimalFormat.format(couponModel.getRate() * 100);
        this.investUpperLimit = AmountConverter.convertCentToString(couponModel.getInvestUpperLimit());
        this.shared = couponModel.isShared();
        this.birthdayRate = String.valueOf(couponModel.getBirthdayBenefit());

    }

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(String investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public ProductType getLoanProductType() {
        return loanProductType;
    }

    public void setLoanProductType(ProductType loanProductType) {
        this.loanProductType = loanProductType;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInvestUpperLimit() {
        return investUpperLimit;
    }

    public void setInvestUpperLimit(String investUpperLimit) {
        this.investUpperLimit = investUpperLimit;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getBirthdayRate() {
        return birthdayRate;
    }

    public void setBirthdayRate(String birthdayRate) {
        this.birthdayRate = birthdayRate;
    }
}
