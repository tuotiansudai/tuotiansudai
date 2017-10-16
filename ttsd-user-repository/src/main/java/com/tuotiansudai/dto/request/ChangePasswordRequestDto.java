package com.tuotiansudai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequestDto extends ResetPasswordRequestDto {
    @JsonProperty("ori_password")
    private String oriPassword;

    public ChangePasswordRequestDto(String loginName, String oriPassword, String password) {
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
