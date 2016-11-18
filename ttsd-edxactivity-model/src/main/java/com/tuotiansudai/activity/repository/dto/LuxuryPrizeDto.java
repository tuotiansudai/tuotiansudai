package com.tuotiansudai.activity.repository.dto;


import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;

import java.io.Serializable;

public class LuxuryPrizeDto implements Serializable {

    private long id;

    private String brand;

    private String name;

    private String price;

    private String image;

    private String introduce;

    private long investAmount;

    private long tenPercentOffInvestAmount;

    private long twentyPercentOffInvestAmount;

    private long thirtyPercentOffInvestAmount;

    public LuxuryPrizeDto(LuxuryPrizeModel luxuryPrizeModel) {
        this.id = luxuryPrizeModel.getId();
        this.brand = luxuryPrizeModel.getBrand();
        this.name = luxuryPrizeModel.getName();
        this.price = luxuryPrizeModel.getPrice();
        this.image = luxuryPrizeModel.getImage();
        this.introduce = luxuryPrizeModel.getIntroduce();
        this.investAmount = luxuryPrizeModel.getInvestAmount();
        this.tenPercentOffInvestAmount = luxuryPrizeModel.getTenPercentOffInvestAmount();
        this.twentyPercentOffInvestAmount = luxuryPrizeModel.getTwentyPercentOffInvestAmount();
        this.thirtyPercentOffInvestAmount = luxuryPrizeModel.getThirtyPercentOffInvestAmount();
    }

    public long getId() {
        return id;
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

    public long getInvestAmount() {
        return investAmount;
    }

    public long getTenPercentOffInvestAmount() {
        return tenPercentOffInvestAmount;
    }

    public long getTwentyPercentOffInvestAmount() {
        return twentyPercentOffInvestAmount;
    }

    public long getThirtyPercentOffInvestAmount() {
        return thirtyPercentOffInvestAmount;
    }
}
