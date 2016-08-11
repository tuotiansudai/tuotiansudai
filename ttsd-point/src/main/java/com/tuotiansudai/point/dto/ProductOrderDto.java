package com.tuotiansudai.point.dto;


import com.tuotiansudai.point.repository.model.ProductOrderModel;

import java.util.Date;

public class ProductOrderDto {
    private long productOrderId;
    private String loginName;
    private Date createdTime;
    private long usedCount;
    private String realName;
    private String mobile;
    private String address;
    private String consignmentTime;

    public ProductOrderDto(ProductOrderModel productOrderModel, long usedCount) {
        this.productOrderId = productOrderModel.getId();
        this.loginName = productOrderModel.getCreatedBy();
        this.createdTime = productOrderModel.getCreatedTime();
        this.usedCount = usedCount;
        this.realName = productOrderModel.getRealName();
        this.mobile = productOrderModel.getMobile();
        this.consignmentTime = productOrderModel.getConsignmentTime();
        this.address = productOrderModel.getAddress();
    }

    public long getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(long productOrderId) {
        this.productOrderId = productOrderId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsignmentTime() {
        return consignmentTime;
    }

    public void setConsignmentTime(String consignmentTime) {
        this.consignmentTime = consignmentTime;
    }
}
