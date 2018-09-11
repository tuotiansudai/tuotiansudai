package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class RegisterAccountDto implements Serializable {

    private String loginName;

    private String mobile;

    @NotEmpty
    private String userName;

    @NotEmpty
    @Pattern(regexp = "^[1-9]\\d{13,16}[a-zA-Z0-9]$")
    private String identityNumber;

    @NotNull
    private Source source;

    public RegisterAccountDto() {
    }

    public RegisterAccountDto(String loginName, String mobile, String userName, String identityNumber, Source source) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.source = source;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName.trim();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNumber() {
        return identityNumber.trim();
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
