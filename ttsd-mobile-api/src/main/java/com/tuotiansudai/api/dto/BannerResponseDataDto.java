package com.tuotiansudai.api.dto;

import java.util.ArrayList;
import java.util.List;

public class BannerResponseDataDto extends BaseResponseDataDto{

    private String bannerId;

    private String bannerDescription;

    private List<BannerPictureResponseDataDto> pictures = new ArrayList<BannerPictureResponseDataDto>(0);

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerDescription() {
        return bannerDescription;
    }

    public void setBannerDescription(String bannerDescription) {
        this.bannerDescription = bannerDescription;
    }

    public List<BannerPictureResponseDataDto> getPictures() {
        return pictures;
    }

    public void setPictures(List<BannerPictureResponseDataDto> pictures) {
        this.pictures = pictures;
    }
}
