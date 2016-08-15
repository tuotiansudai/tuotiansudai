package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class ProductOrderModel implements Serializable{

    private long id;
    private long productId;
    private long productPrice;
    private Integer num;
    private long totalPrice;
    private String realName;
    private String mobile;
    private String address;
    private boolean consignment;
    private String consignmentTime;
    private String createdBy;
    private Date createdTime;

    public ProductOrderModel(){

    }

    public ProductOrderModel(long productId, long productPrice, Integer num, long totalPrice, String realName, String mobile, String address, boolean consignment, String consignmentTime, String createdBy) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.num = num;
        this.totalPrice = totalPrice;
        this.realName = realName;
        this.mobile = mobile;
        this.address = address;
        this.consignment = consignment;
        this.consignmentTime = consignmentTime;
        this.createdBy = createdBy;
        this.createdTime = new Date();
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

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
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

    public boolean isConsignment() {
        return consignment;
    }

    public void setConsignment(boolean consignment) {
        this.consignment = consignment;
    }

    public String getConsignmentTime() {
        return consignmentTime;
    }

    public void setConsignmentTime(String consignmentTime) {
        this.consignmentTime = consignmentTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
