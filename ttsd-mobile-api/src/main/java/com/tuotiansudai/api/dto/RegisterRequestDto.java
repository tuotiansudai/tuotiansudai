package com.tuotiansudai.api.dto;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RegisterRequestDto extends BaseParamDto {
    @NotEmpty(message = "0005")
    @Pattern(regexp = "(?!^\\d+$)^\\w{5,25}$", message = "0006")
    private String userName;
    @NotEmpty(message = "0001")
    @Pattern(regexp = "^1\\d{10}$", message = "0002")
    private String phoneNum;
    @NotEmpty(message = "0009")
    @Pattern(regexp = "^[0-9]{6}$", message = "0009")
    private String captcha;
    @NotEmpty(message = "0012")
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$", message = "0012")
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

    public RegisterUserDto convertToRegisterUserDto(){
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setLoginName(this.getUserName());
        registerUserDto.setMobile(this.getPhoneNum());
        registerUserDto.setPassword(this.getPassword());
        registerUserDto.setCaptcha(this.getCaptcha());
        registerUserDto.setReferrer(this.getReferrer());
        registerUserDto.setSource(Source.valueOf(getBaseParam().getPlatform().toUpperCase()));
        return registerUserDto;
    }

}
