package com.tuotiansudai.activity.repository.dto;


import com.tuotiansudai.activity.repository.model.TravelPrizeModel;

import java.io.Serializable;

public class TravelPrizeDto implements Serializable {

    private long id;

    private String name;

    private String price;

    private String image;

    private String introduce;

    private long investAmount;

    public TravelPrizeDto(TravelPrizeModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.price = model.getPrice();
        this.image = model.getImage();
        this.introduce = model.getIntroduce();
        this.investAmount = model.getInvestAmount();
    }

    public long getId() {
        return id;
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
}
