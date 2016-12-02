package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AnxinSignSendCaptchaRequestDto extends BaseParamDto {

    /**
     * 是不是语音验证码
     */
    @ApiModelProperty(value = "是不是语音验证码", example = "true：是 ，false：不是")
    boolean isVoice;

    public boolean isVoice() {
        return isVoice;
    }

    public void setIsVoice(boolean isVoice) {
        this.isVoice = isVoice;
    }
}
