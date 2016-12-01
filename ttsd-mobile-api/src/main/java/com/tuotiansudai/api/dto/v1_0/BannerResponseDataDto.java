package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class BannerResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "bannerId", example = "null")
    private String bannerId;

    @ApiModelProperty(value = "banner描述", example = "null")
    private String bannerDescription;

    @ApiModelProperty(value = "banner列表", example = "list")
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
