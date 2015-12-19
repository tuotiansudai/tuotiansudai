package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LoginDto extends BaseDataDto {

    private boolean isLocked;

    private boolean isCaptchaNotMatch;

    private List<String> roles;

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

    @JsonProperty(value = "roles")
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
