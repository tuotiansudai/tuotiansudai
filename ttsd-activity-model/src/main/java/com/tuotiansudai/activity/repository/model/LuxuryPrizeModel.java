package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class LuxuryPrizeModel implements Serializable{

    private static final long serialVersionUID = -4905565481181941515L;

    private long id;

    private String brand;

    private String name;

    private String price;

    private String image;

    private long investAmount;

    private long tenPercentOffInvestAmount;

    private long twentyPercentOffInvestAmount;

    private long thirtyPercentOffInvestAmount;

    private String introduce;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getTenPercentOffInvestAmount() {
        return tenPercentOffInvestAmount;
    }

    public void setTenPercentOffInvestAmount(long tenPercentOffInvestAmount) {
        this.tenPercentOffInvestAmount = tenPercentOffInvestAmount;
    }

    public long getTwentyPercentOffInvestAmount() {
        return twentyPercentOffInvestAmount;
    }

    public void setTwentyPercentOffInvestAmount(long twentyPercentOffInvestAmount) {
        this.twentyPercentOffInvestAmount = twentyPercentOffInvestAmount;
    }

    public long getThirtyPercentOffInvestAmount() {
        return thirtyPercentOffInvestAmount;
    }

    public void setThirtyPercentOffInvestAmount(long thirtyPercentOffInvestAmount) {
        this.thirtyPercentOffInvestAmount = thirtyPercentOffInvestAmount;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
