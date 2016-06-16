package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

public class MysteriousPrizeDto implements Serializable{
    private String prizeName;
    private String imageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String prizeDate;

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrizeDate() {
        return prizeDate;
    }

    public void setPrizeDate(String prizeDate) {
        this.prizeDate = prizeDate;
    }
}
