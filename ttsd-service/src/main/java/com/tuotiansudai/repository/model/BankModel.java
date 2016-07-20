package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class BankModel implements Serializable {

    private long id;

    private String name;

    private String shorterName;

    private String imageUrl;

    private long singleAmount;

    private long singleDayAmount;

    private String createdBy;

    private Date createdTime = new Date();

    private String updatedBy;

    private Date updatedTime;

    public BankModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShorterName() {
        return shorterName;
    }

    public void setShorterName(String shorterName) {
        this.shorterName = shorterName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(long singleAmount) {
        this.singleAmount = singleAmount;
    }

    public long getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(long singleDayAmount) {
        this.singleDayAmount = singleDayAmount;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
