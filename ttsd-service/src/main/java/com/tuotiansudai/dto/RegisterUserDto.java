package com.tuotiansudai.dto;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.UserModel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

public class RegisterUserDto {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{5,25}$")
    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^1\\d{10}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{6}$")
    private String captcha;

    @NotEmpty
    private String password;

    private String referrer;

    @AssertTrue
    private boolean agreement;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public boolean getAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public UserModel convertToUserModel() {
        UserModel userModel = new UserModel();
        userModel.setLoginName(this.loginName);
        userModel.setMobile(this.mobile);
        userModel.setPassword(this.password);
        if (!Strings.isNullOrEmpty(this.referrer)) {
            userModel.setReferrer(this.referrer);
        }
        return userModel;
    }

}
