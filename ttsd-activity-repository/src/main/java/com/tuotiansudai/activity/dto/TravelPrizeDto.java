package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.List;

public class TravelPrizeDto implements Serializable {

    private long id;

    private String name;

    private String description;

    private String price;

    private String image;

    private List<String> introduce;

    private String investAmount;

    public TravelPrizeDto(TravelPrizeModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.description = model.getDescription();
        this.price = model.getPrice();
        this.image = model.getImage();
        this.introduce = model.getIntroduce();
        this.investAmount = AmountConverter.convertCentToString(model.getInvestAmount());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public List<String> getIntroduce() {
        return introduce;
    }

    public String getInvestAmount() {
        return investAmount;
    }
}
