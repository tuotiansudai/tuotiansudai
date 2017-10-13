package com.tuotiansudai.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRestChangePasswordRequestDto extends UserRestResetPasswordRequestDto {
    @JsonProperty("ori_password")
    private String oriPassword;

    public UserRestChangePasswordRequestDto(String loginName, String oriPassword, String password) {
        super(loginName, password);
        this.oriPassword = oriPassword;
    }

    public String getOriPassword() {
        return oriPassword;
    }

    public void setOriPassword(String oriPassword) {
        this.oriPassword = oriPassword;
    }
}
