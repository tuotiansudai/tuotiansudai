package com.tuotiansudai.api.dto;

public class ImageCaptchaResponseDataDto extends BaseResponseDataDto {
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
