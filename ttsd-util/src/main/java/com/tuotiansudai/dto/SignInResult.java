package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SignInResult implements Serializable {

    private boolean result;

    private String message;

    private SignInUserInfo userInfo;

    private String token;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SignInUserInfo getUserInfo() {
        return userInfo;
    }

    @JsonProperty(value = "user_info")
    public void setUserInfo(SignInUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
