package com.tuotiansudai.api.dto;

public class ImageCaptureResponseDataDto extends BaseResponseDataDto {
    private String imageCapture;

    public String getImageCapture() {
        return imageCapture;
    }

    public void setImageCapture(String imageCapture) {
        this.imageCapture = imageCapture;
    }
    public ImageCaptureResponseDataDto(String imageCapture){
        this.imageCapture = imageCapture;
    }
    public ImageCaptureResponseDataDto(){

    }
}
