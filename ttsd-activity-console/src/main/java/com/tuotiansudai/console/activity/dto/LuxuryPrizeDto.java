package com.tuotiansudai.console.activity.dto;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class LuxuryPrizeDto implements Serializable{
    private static final long serialVersionUID = -8102560580155869158L;
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
    public LuxuryPrizeDto(){}

    public LuxuryPrizeDto(LuxuryPrizeModel luxuryPrizeModel){
        this.luxuryPrizeId = luxuryPrizeModel.getId();
        this.brand = luxuryPrizeModel.getBrand();
        this.name = luxuryPrizeModel.getName();
        this.price = luxuryPrizeModel.getPrice();
        this.image = luxuryPrizeModel.getImage();
        this.introduce = luxuryPrizeModel.getIntroduce();
        this.investAmount = AmountConverter.convertCentToString(luxuryPrizeModel.getInvestAmount());
        this.tenPercentOffInvestAmount = AmountConverter.convertCentToString(luxuryPrizeModel.getTenPercentOffInvestAmount());
        this.twentyPercentOffInvestAmount = AmountConverter.convertCentToString(luxuryPrizeModel.getTwentyPercentOffInvestAmount());
        this.thirtyPercentOffInvestAmount = AmountConverter.convertCentToString(luxuryPrizeModel.getThirtyPercentOffInvestAmount());
    }

    public long getLuxuryPrizeId() {
        return luxuryPrizeId;
    }

    public void setLuxuryPrizeId(long luxuryPrizeId) {
        this.luxuryPrizeId = luxuryPrizeId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getTenPercentOffInvestAmount() {
        return tenPercentOffInvestAmount;
    }

    public void setTenPercentOffInvestAmount(String tenPercentOffInvestAmount) {
        this.tenPercentOffInvestAmount = tenPercentOffInvestAmount;
    }

    public String getTwentyPercentOffInvestAmount() {
        return twentyPercentOffInvestAmount;
    }

    public void setTwentyPercentOffInvestAmount(String twentyPercentOffInvestAmount) {
        this.twentyPercentOffInvestAmount = twentyPercentOffInvestAmount;
    }

    public String getThirtyPercentOffInvestAmount() {
        return thirtyPercentOffInvestAmount;
    }

    public void setThirtyPercentOffInvestAmount(String thirtyPercentOffInvestAmount) {
        this.thirtyPercentOffInvestAmount = thirtyPercentOffInvestAmount;
    }
}
