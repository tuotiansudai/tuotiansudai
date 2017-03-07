package com.tuotiansudai.dto;

import java.io.Serializable;

public class AnxinSendCaptchaDto implements Serializable {

    private String loginName;
    private boolean isVoice;

    public AnxinSendCaptchaDto() {
    }

    public AnxinSendCaptchaDto(String loginName, boolean isVoice) {
        this.loginName = loginName;
        this.isVoice = isVoice;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }
}
