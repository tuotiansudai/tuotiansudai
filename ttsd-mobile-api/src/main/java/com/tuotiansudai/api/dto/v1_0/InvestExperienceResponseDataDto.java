package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ProductType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class InvestExperienceResponseDataDto {

    @ApiModelProperty(value = "优惠券id", example = "1001")
    private String userCouponId;

    @ApiModelProperty(value = "优惠券类型", example = "RED_ENVELOPE")
    private String type;

    @ApiModelProperty(value = "优惠券名称", example = "投资红包")
    private String name;

    @ApiModelProperty(value = "优惠券金额", example = "5000")
    private String amount;

    @ApiModelProperty(value = "利率(加息券专用)", example = "0.8")
    private String rate;

    @ApiModelProperty(value = "优惠券开始时间", example = "2016-01-01 00:00:00")
    private String startDate;

    @ApiModelProperty(value = "优惠券结束时间", example = "2016-01-01 00:00:01")
    private String endDate;

    @ApiModelProperty(value = "最少投资额", example = "100000")
    private String investLowerLimit;

    @ApiModelProperty(value = "标的类型", example = "30,90,180,360")
    private List<ProductType> productNewTypes;

    @ApiModelProperty(value = "优惠券来源", example = "拓天速贷赠送")
    private String couponSource;

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(String investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public List<ProductType> getProductNewTypes() {
        return productNewTypes;
    }

    public void setProductNewTypes(List<ProductType> productNewTypes) {
        this.productNewTypes = productNewTypes;
    }

    public String getCouponSource() {
        return couponSource;
    }

    public void setCouponSource(String couponSource) {
        this.couponSource = couponSource;
    }
}
