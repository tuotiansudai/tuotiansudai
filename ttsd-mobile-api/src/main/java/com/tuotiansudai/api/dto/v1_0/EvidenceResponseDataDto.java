package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class EvidenceResponseDataDto {

    @ApiModelProperty(value = "标题", example = "身份证")
    private String title;

    @ApiModelProperty(value = "图片地址", example = "url")
    private List<String> imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
