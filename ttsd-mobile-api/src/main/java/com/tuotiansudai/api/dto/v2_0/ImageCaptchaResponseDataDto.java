package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;

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
