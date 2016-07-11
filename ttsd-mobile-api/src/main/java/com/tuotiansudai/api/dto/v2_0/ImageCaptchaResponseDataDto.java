package com.tuotiansudai.api.dto.v2_0;


public class ImageCaptchaResponseDataDto extends BaseResponseDataDto {
    private String imageCaptcha;

    public ImageCaptchaResponseDataDto(String imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
    }

    public String getImageCaptcha() {
        return imageCaptcha;
    }

    public void setImageCaptcha(String imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
    }

}
