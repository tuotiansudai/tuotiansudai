package com.tuotiansudai.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserRestResetPasswordRequestDto implements Serializable {
    @JsonProperty("login_name")
    private String loginName;
    private String password;

    public UserRestResetPasswordRequestDto(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
