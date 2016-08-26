package com.tuotiansudai.console.activity.dto;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class LuxuryPrizeRequestDto implements Serializable {

    private long luxuryPrizeId;

    private String brand;

    private String name;

    private String price;

    private String image;

    private String introduce;

    private String investAmount;

    private String tenPercentOffInvestAmount;

    private String twentyPercentOffInvestAmount;

    private String thirtyPercentOffInvestAmount;

    public long getLuxuryPrizeId() {
        return luxuryPrizeId;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public String getTenPercentOffInvestAmount() {
        return tenPercentOffInvestAmount;
    }

    public String getTwentyPercentOffInvestAmount() {
        return twentyPercentOffInvestAmount;
    }

    public String getThirtyPercentOffInvestAmount() {
        return thirtyPercentOffInvestAmount;
    }

    public void setLuxuryPrizeId(long luxuryPrizeId) {
        this.luxuryPrizeId = luxuryPrizeId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public void setTenPercentOffInvestAmount(String tenPercentOffInvestAmount) {
        this.tenPercentOffInvestAmount = tenPercentOffInvestAmount;
    }

    public void setTwentyPercentOffInvestAmount(String twentyPercentOffInvestAmount) {
        this.twentyPercentOffInvestAmount = twentyPercentOffInvestAmount;
    }

    public void setThirtyPercentOffInvestAmount(String thirtyPercentOffInvestAmount) {
        this.thirtyPercentOffInvestAmount = thirtyPercentOffInvestAmount;
    }
}
