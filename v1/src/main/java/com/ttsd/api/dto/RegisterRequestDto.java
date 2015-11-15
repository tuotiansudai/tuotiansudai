package com.ttsd.api.dto;


import com.esoft.archer.user.model.User;

public class RegisterRequestDto extends BaseParamDto {
    private String userName;
    private String phoneNum;
    private String captcha;
    private String password;
    private String referrer;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    public User convertToUser(){
        User user = new User();
        user.setId(this.getUserName().toLowerCase());
        user.setUsername(this.getUserName().toLowerCase());
        user.setMobileNumber(this.getPhoneNum());
        user.setPassword(this.password);
        user.setReferrer(this.getReferrer());
        return user;
    }
}
