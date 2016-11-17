package com.tuotiansudai.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class BaseCouponResponseDataDto {

    protected String userCouponId;

    protected CouponType type;

    protected String name;

    protected String amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date endDate;

    protected String investLowerLimit;

    protected List<String> productTypes;

    protected String rate;

    protected String investUpperLimit = "1000000.00";

    protected boolean shared;

    protected String investAmount;

    protected String birthdayRate;

    protected List<String> productNewTypes;

    protected String couponSource;

    public BaseCouponResponseDataDto() {
    }

    public BaseCouponResponseDataDto(UserCouponModel userCouponModel) {
        CouponModel couponModel = userCouponModel.getCoupon();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.userCouponId = String.valueOf(userCouponModel.getId());
        this.type = couponModel.getCouponType();
        this.name = couponModel.getCouponType().getName();
        this.amount = AmountConverter.convertCentToString(couponModel.getAmount()).replaceAll("\\.00","");
        this.startDate = userCouponModel.getStartTime();
        this.endDate = userCouponModel.getEndTime();
        this.investLowerLimit = AmountConverter.convertCentToString(couponModel.getInvestLowerLimit());
        this.productTypes = Lists.transform(couponModel.getProductTypes(), new Function<ProductType, String>() {
            @Override
            public String apply(ProductType input) {
                return input.getProductLine();
            }
        });
        this.rate = decimalFormat.format(couponModel.getRate() * 100);
        this.shared = couponModel.isShared();
        this.birthdayRate = String.valueOf(couponModel.getBirthdayBenefit());
        this.productNewTypes = Lists.transform(couponModel.getProductTypes(), new Function<ProductType, String>() {
            @Override
            public String apply(ProductType input) {
                return input.name();
            }
        });
        this.couponSource = couponModel.getCouponSource();
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

    public List<String> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<String> productTypes) {
        this.productTypes = productTypes;
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

    public List<String> getProductNewTypes() { return productNewTypes; }

    public void setProductNewTypes(List<String> productNewTypes) { this.productNewTypes = productNewTypes; }

    public String getCouponSource() {
        return couponSource;
    }

    public void setCouponSource(String couponSource) {
        this.couponSource = couponSource;
    }
}
