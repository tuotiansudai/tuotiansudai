package com.tuotiansudai.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class BaseCouponResponseDataDto {

    @ApiModelProperty(value = "优惠券id", example = "1001")
    protected String userCouponId;

    @ApiModelProperty(value = "优惠券类型", example = "RED_ENVELOPE")
    protected CouponType type;

    @ApiModelProperty(value = "优惠券名称", example = "投资红包")
    protected String name;

    @ApiModelProperty(value = "优惠券金额", example = "5000")
    protected String amount;

    @ApiModelProperty(value = "优惠券加息期数", example = "1")
    protected Integer period;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @ApiModelProperty(value = "优惠券开始时间", example = "2016-01-01 00:00:00")
    protected Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "优惠券结束时间", example = "2016-01-01 00:00:01")
    protected Date endDate;

    @ApiModelProperty(value = "最少投资额", example = "100000")
    protected String investLowerLimit;

    @ApiModelProperty(value = "标的类型", example = "SYL")
    protected List<String> productTypes;

    @ApiModelProperty(value = "利率(加息券专用)", example = "0.8")
    protected String rate;

    @ApiModelProperty(value = "investUpperLimit", example = "1000000.00")
    protected String investUpperLimit = "1000000.00";

    @ApiModelProperty(value = "共享", example = "false")
    protected boolean shared;

    @ApiModelProperty(value = "投资金额", example = "1000")
    protected String investAmount;

    @ApiModelProperty(value = "生日月利率", example = "10")
    protected String birthdayRate;

    @ApiModelProperty(value = "标的类型", example = "30,90,180,360")
    protected List<String> productNewTypes;

    @ApiModelProperty(value = "来源", example = "拓天速贷赠送")
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
        this.period = couponModel.getPeriod();
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
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
