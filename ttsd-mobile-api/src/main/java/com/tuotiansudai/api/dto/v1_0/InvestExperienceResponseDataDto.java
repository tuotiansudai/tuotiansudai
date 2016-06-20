package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.ProductType;

import java.util.List;

public class InvestExperienceResponseDataDto {

    private String userCouponId;

    private String type;

    private String name;

    private String amount;

    private String rate;

    private String startDate;

    private String endDate;

    private String investLowerLimit;

    private List<ProductType> productNewTypes;

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

}
