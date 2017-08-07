package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRequestDto {

    @JsonProperty(value = "login_name")
    private String loginName;

    private String username;

    private String mobile;

    public AccountRequestDto(String loginName, String username, String mobile) {
        this.loginName = loginName;
        this.username = username;
        this.mobile = mobile;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }
}
