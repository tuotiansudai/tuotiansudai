package com.tuotiansudai.dto;


public class SignInDto extends BaseDataDto {

    private String username;

    private String password;

    private String captcha;

    private String source;

    private String deviceId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public SignInDto() {

    }

    public SignInDto(String username, String password, String captcha, String source, String deviceId) {
        this.username = username;
        this.password = password;
        this.captcha = captcha;
        this.source = source;
        this.deviceId = deviceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
