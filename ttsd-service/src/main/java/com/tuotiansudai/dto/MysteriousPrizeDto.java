package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.Date;

public class MysteriousPrizeDto implements Serializable{
    private String prizeName;
    private String imageUrl;
    private Date prizeDate;

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

    public Date getPrizeDate() {
        return prizeDate;
    }

    public void setPrizeDate(Date prizeDate) {
        this.prizeDate = prizeDate;
    }
}
