package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto extends BaseDataDto {

    private boolean isLocked;

    private boolean isCaptchaNotMatch;

    @JsonProperty(value = "isLocked")
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @JsonProperty(value = "isCaptchaNotMatch")
    public boolean isCaptchaNotMatch() {
        return isCaptchaNotMatch;
    }

    public void setCaptchaNotMatch(boolean captchaNotMatch) {
        isCaptchaNotMatch = captchaNotMatch;
    }
}
