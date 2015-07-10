package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.UserModel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RegisterUserDto {

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{5,25}$")
    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{6}$")
    private String captcha;

    @NotEmpty
    private String password;

    private String referrer;

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

    public UserModel convertToUserModel() {
        UserModel userModel = new UserModel();
        userModel.setLoginName(this.loginName);
        userModel.setMobile(this.mobile);
        userModel.setPassword(this.password);
        userModel.setReferrer(this.referrer);
        return userModel;
    }

}
