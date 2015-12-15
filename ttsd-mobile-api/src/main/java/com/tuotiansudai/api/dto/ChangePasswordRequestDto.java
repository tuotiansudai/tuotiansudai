package com.tuotiansudai.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class ChangePasswordRequestDto extends BaseParamDto {
    private String originPassword;


    @NotEmpty(message = "0012")
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$", message = "0012")
    private String newPassword;

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
