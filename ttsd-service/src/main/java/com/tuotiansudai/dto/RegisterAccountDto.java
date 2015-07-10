package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RegisterAccountDto {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{5,25}$")
    private String loginName;

    @NotEmpty
    private String userName;

    @NotEmpty
    @Pattern(regexp = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}")
    private String identityNumber;

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
