package com.ttsd.api.dto;

import com.esoft.archer.banner.model.BannerPicture;

import java.util.ArrayList;
import java.util.List;

public class BannerResponseDataDto extends BaseResponseDataDto{
    private String id;
    private String description;
    private List<BannerPictureResponseDataDto> pictures = new ArrayList<BannerPictureResponseDataDto>(0);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BannerPictureResponseDataDto> getPictures() {
        return pictures;
    }

    public void setPictures(List<BannerPictureResponseDataDto> pictures) {
        this.pictures = pictures;
    }
}
