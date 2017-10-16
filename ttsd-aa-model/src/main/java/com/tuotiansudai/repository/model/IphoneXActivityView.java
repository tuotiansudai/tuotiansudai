package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class IphoneXActivityView implements Serializable{

    private String loginName;
    private String userName;
    private String mobile;
    private long sumAmount;
    private ProductType productType;

    public IphoneXActivityView() {
    }

    public IphoneXActivityView(String loginName, String userName, String mobile, long sumAmount, ProductType productType) {
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.sumAmount = sumAmount;
        this.productType = productType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
