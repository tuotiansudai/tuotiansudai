package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class ImageCaptchaResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "验证码", example = "12345")
    private String imageCaptcha;

    public String getImageCaptcha() {
        return imageCaptcha;
    }

    public void setImageCaptcha(String imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
    }

    public ImageCaptchaResponseDataDto(String imageCaptcha){
        this.imageCaptcha = imageCaptcha;
    }
    public ImageCaptchaResponseDataDto(){

    }
}
