package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class ActivityRegisterRequestDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^1\\d{10}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$")
    private String captcha;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$")
    private String password;

    private String referrerMobile;

    private String channel;

    private Source source = Source.MOBILE;


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

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public RegisterUserDto convertToRegisterUserDto() {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setMobile(getMobile());
        userDto.setCaptcha(getCaptcha());
        userDto.setPassword(getPassword());
        userDto.setReferrer(getReferrerMobile());
        userDto.setChannel(getChannel());
        userDto.setSource(getSource());
        return userDto;
    }
}
