package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RegisterHuizuRequestDto {

    @NotEmpty(message = "0001")
    @Pattern(regexp = "^1\\d{10}$", message = "0002")
    @ApiModelProperty(value = "手机号", example = "13900000022")
    private String phoneNum;

    @NotEmpty(message = "0012")
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$", message = "0012")
    @ApiModelProperty(value = "密码", example = "123abc")
    private String password;

    private Source source = Source.HUI_ZU;

    private String channel = "HUI_ZU";

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public RegisterUserDto convertToRegisterUserDto() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setMobile(this.getPhoneNum());
        registerUserDto.setPassword(this.getPassword());
        registerUserDto.setSource(this.getSource());
        registerUserDto.setChannel(this.getChannel());
        return registerUserDto;
    }
}
