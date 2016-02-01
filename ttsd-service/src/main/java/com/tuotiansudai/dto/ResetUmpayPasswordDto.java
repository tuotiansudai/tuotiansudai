package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ResetUmpayPasswordDto implements Serializable{

    @NotEmpty
    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^[1-9]\\d{13,16}[a-zA-Z0-9]$")
    private String identityNumber;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public ResetUmpayPasswordDto(String loginName, String identityNumber) {
        this.loginName = loginName;
        this.identityNumber = identityNumber;
    }

    public ResetUmpayPasswordDto() {

    }
}
