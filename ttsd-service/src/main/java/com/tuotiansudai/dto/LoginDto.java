package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Role;

import java.util.List;

public class LoginDto extends BaseDataDto {

    private boolean isLocked;

    private boolean isCaptchaNotMatch;

    private List<Role> roles;

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
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
