package com.tuotiansudai.point.repository.model;

import java.io.Serializable;
import java.util.Date;

public class ProductOrderModel implements Serializable{

    private long id;
    private long productId;
    private long points;
    private long actualPoints;
    private Integer num;
    private long totalPoints;
    private String contact;
    private String mobile;
    private String address;
    private String comment;
    private boolean consignment;
    private Date consignmentTime;
    private String createdBy;
    private Date createdTime;

    public ProductOrderModel(){

    }

    public ProductOrderModel(long productId, long points, long actualPoints, Integer num, long totalPoints, String contact, String mobile, String address, String comment, boolean consignment, Date consignmentTime, String createdBy) {
        this.productId = productId;
        this.points = points;
        this.actualPoints = actualPoints;
        this.num = num;
        this.totalPoints = totalPoints;
        this.contact = contact;
        this.mobile = mobile;
        this.address = address;
        this.comment = comment;
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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public long getActualPoints() {
        return actualPoints;
    }

    public void setActualPoints(long actualPoints) {
        this.actualPoints = actualPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public boolean isConsignment() {
        return consignment;
    }

    public void setConsignment(boolean consignment) {
        this.consignment = consignment;
    }

    public Date getConsignmentTime() {
        return consignmentTime;
    }

    public void setConsignmentTime(Date consignmentTime) {
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
