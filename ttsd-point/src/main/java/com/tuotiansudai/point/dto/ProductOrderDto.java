package com.tuotiansudai.point.dto;


import com.tuotiansudai.point.repository.model.ProductOrderModel;

import java.util.Date;

public class ProductOrderDto {
    private long id;
    private long productId;
    private String loginName;
    private Date createdTime;
    private long num;
    private String realName;
    private String mobile;
    private String address;
    private Date consignmentTime;
    private boolean consignment;

    public ProductOrderDto(ProductOrderModel productOrderModel) {
        this.id = productOrderModel.getId();
        this.productId = productOrderModel.getProductId();
        this.loginName = productOrderModel.getCreatedBy();
        this.createdTime = productOrderModel.getCreatedTime();
        this.num = productOrderModel.getNum();
        this.realName = productOrderModel.getRealName();
        this.mobile = productOrderModel.getMobile();
        this.consignmentTime = productOrderModel.getConsignmentTime();
        this.address = productOrderModel.getAddress();
        this.consignment = productOrderModel.isConsignment();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
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

    public Date getConsignmentTime() {
        return consignmentTime;
    }

    public void setConsignmentTime(Date consignmentTime) {
        this.consignmentTime = consignmentTime;
    }

    public boolean isConsignment() {
        return consignment;
    }

    public void setConsignment(boolean consignment) {
        this.consignment = consignment;
    }
}
