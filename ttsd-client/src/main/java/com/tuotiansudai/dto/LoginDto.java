package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

public class LoginDto implements Serializable {

    protected boolean status;

    protected String message;

    private boolean isLocked;

    private boolean isCaptchaNotMatch;

    private String newSessionId;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public String getNewSessionId() {
        return newSessionId;
    }

    public void setNewSessionId(String newSessionId) {
        this.newSessionId = newSessionId;
    }
}
