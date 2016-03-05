package com.tuotiansudai.api.dto;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;

import java.util.List;

public class PointExchangeRecordResponseDataDto {

    private String couponId;
    private CouponType couponType;
    private String name;
    private String amount;
    private String rate;
    private String investLowerLimit;
    private String investUpperLimit;
    private Integer deadline;
    private long point;
    private List<ProductType> productTypes;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
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

    public String getRate() { return rate; }

    public void setRate(String rate) { this.rate = rate; }

    public String getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(String investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public String getInvestUpperLimit() {
        return investUpperLimit;
    }

    public void setInvestUpperLimit(String investUpperLimit) {
        this.investUpperLimit = investUpperLimit;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public PointExchangeRecordResponseDataDto(){

    }
    public PointExchangeRecordResponseDataDto(CouponModel input, long point){
        this.setCouponId(String.valueOf(input.getId()));
        this.setCouponType(input.getCouponType());
        this.setName(input.getCouponType().getName());
        this.setAmount(AmountConverter.convertCentToString(input.getAmount()));
        this.setRate(String.valueOf(input.getRate()));
        this.setInvestLowerLimit(AmountConverter.convertCentToString(input.getInvestLowerLimit()));
        this.setInvestUpperLimit(AmountConverter.convertCentToString(input.getInvestUpperLimit()));
        this.setProductTypes(input.getProductTypes());
        this.setPoint(point);
        this.setDeadline(input.getDeadline()==null?0:input.getDeadline());
    }

}
