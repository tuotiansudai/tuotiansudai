package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.UserModel;

public class RegisterDto {
    private String loginName;

    private String mobileNumber;

    private String captcha;

    private String email;

    private String password;

    private String referrer;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        userModel.setMobileNumber(this.mobileNumber);
        userModel.setPassword(this.password);
        userModel.setEmail(this.email);
        return userModel;
    }

}
