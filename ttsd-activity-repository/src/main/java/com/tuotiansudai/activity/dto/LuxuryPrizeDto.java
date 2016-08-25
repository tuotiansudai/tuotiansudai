package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class LuxuryPrizeDto implements Serializable {

    private static final long serialVersionUID = -8102560580155869158L;

    private long id;

    private String brand;

    private String name;

    private String price;

    private String image;

    private String introduce;

    private String investAmount;

    public LuxuryPrizeDto(LuxuryPrizeModel luxuryPrizeModel) {
        this.id = luxuryPrizeModel.getId();
        this.brand = luxuryPrizeModel.getBrand();
        this.name = luxuryPrizeModel.getName();
        this.price = AmountConverter.convertCentToString(luxuryPrizeModel.getPrice());
        this.image = luxuryPrizeModel.getImage();
        this.introduce = luxuryPrizeModel.getIntroduce();
        this.investAmount = AmountConverter.convertCentToString(luxuryPrizeModel.getInvestAmount());
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

    public String getInvestAmount() {
        return investAmount;
    }
}
