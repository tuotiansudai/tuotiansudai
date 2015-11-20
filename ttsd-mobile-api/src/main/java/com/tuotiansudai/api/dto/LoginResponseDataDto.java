package com.tuotiansudai.api.dto;

public class LoginResponseDataDto extends BaseResponseDataDto{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
