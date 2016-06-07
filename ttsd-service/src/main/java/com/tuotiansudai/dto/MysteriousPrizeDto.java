package com.tuotiansudai.dto;

import java.io.Serializable;

public class MysteriousPrizeDto implements Serializable{
    private String prizeName;
    private String imageUrl;

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
}
