package com.tuotiansudai.api.dto.v1_0;

public class AnxinSignSendCaptchaRequestDto extends BaseParamDto {

    /**
     * 是不是语音验证码
     */
    boolean isVoice;

    public boolean isVoice() {
        return isVoice;
    }

    public void setIsVoice(boolean isVoice) {
        this.isVoice = isVoice;
    }
}
