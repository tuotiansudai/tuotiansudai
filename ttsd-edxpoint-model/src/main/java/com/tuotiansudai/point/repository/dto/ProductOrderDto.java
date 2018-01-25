package com.tuotiansudai.point.repository.dto;


import com.tuotiansudai.point.repository.model.ProductOrderModel;

import java.util.Date;

public class ProductOrderDto {
    private long id;
    private long productId;
    private String loginName;
    private Date createdTime;
    private long num;
    private String contact;
    private String mobile;
    private String address;
    private String comment;
    private Date consignmentTime;
    private boolean consignment;
    private long actualPoints;


    public ProductOrderDto(ProductOrderModel productOrderModel) {
        this.id = productOrderModel.getId();
        this.productId = productOrderModel.getProductId();
        this.loginName = productOrderModel.getCreatedBy();
        this.createdTime = productOrderModel.getCreatedTime();
        this.num = productOrderModel.getNum();
        this.contact = productOrderModel.getContact();
        this.mobile = productOrderModel.getMobile();
        this.consignmentTime = productOrderModel.getConsignmentTime();
        this.address = productOrderModel.getAddress();
        this.comment = productOrderModel.getComment();
        this.consignment = productOrderModel.isConsignment();
        this.actualPoints = productOrderModel.getActualPoints();
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public long getActualPoints() {
        return actualPoints;
    }

    public void setActualPoints(long actualPoints) {
        this.actualPoints = actualPoints;
    }
}
