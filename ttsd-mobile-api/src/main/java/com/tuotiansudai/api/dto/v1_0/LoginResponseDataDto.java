package com.tuotiansudai.api.dto.v1_0;

public class LoginResponseDataDto extends BaseResponseDataDto{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
