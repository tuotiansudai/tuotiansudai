package com.tuotiansudai.console.activity.dto;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class LuxuryPrizeDto implements Serializable{
    private static final long serialVersionUID = -8102560580155869158L;
    private long id;
    private String brand;
    private String name;
    private String price;
    private String image;
    private String introduce;
    private String investAmount;

    public LuxuryPrizeDto(){}

    public LuxuryPrizeDto(LuxuryPrizeModel luxuryPrizeModel){
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
}
