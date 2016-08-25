package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class LuxuryPrizeDto implements Serializable {

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

    public LuxuryPrizeDto(LuxuryPrizeModel luxuryPrizeModel) {
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
}
