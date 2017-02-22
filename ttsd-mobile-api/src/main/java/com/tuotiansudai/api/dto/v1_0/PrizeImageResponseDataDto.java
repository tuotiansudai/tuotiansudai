package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class PrizeImageResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "图片地址", example = "{static}/api/images/app-prize-image/app-prize-red.png")
    private String imageUrl;
    @ApiModelProperty(value = "图片位置顺序", example = "1")
    private String seq;
    @ApiModelProperty(value = "图片描述", example = "10元红包")
    private String imageDesc;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }
}
