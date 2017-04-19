package com.tuotiansudai.activity.repository.dto;

import java.io.Serializable;
import java.util.Date;

public class NewmanTyrantPrizeDto implements Serializable{
    private String goldPrizeName;
    private String goldImageUrl;
    private String silverPrizeName;
    private String silverImageUrl;
    private Date prizeDate;

    public String getGoldPrizeName() {
        return goldPrizeName;
    }

    public void setGoldPrizeName(String goldPrizeName) {
        this.goldPrizeName = goldPrizeName;
    }

    public String getGoldImageUrl() {
        return goldImageUrl;
    }

    public void setGoldImageUrl(String goldImageUrl) {
        this.goldImageUrl = goldImageUrl;
    }

    public String getSilverPrizeName() {
        return silverPrizeName;
    }

    public void setSilverPrizeName(String silverPrizeName) {
        this.silverPrizeName = silverPrizeName;
    }

    public String getSilverImageUrl() {
        return silverImageUrl;
    }

    public void setSilverImageUrl(String silverImageUrl) {
        this.silverImageUrl = silverImageUrl;
    }

    public Date getPrizeDate() {
        return prizeDate;
    }

    public void setPrizeDate(Date prizeDate) {
        this.prizeDate = prizeDate;
    }
}
