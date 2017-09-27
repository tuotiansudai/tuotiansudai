package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.ProductType;

import java.io.Serializable;

public class IphoneXActivityDto implements Serializable{

    private String loginName;
    private String mobile;
    private long sumAmount;
    private ProductType productType;

    public IphoneXActivityDto() {
    }

    public IphoneXActivityDto(String loginName, String mobile, long sumAmount, ProductType productType) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.sumAmount = sumAmount;
        this.productType = productType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
