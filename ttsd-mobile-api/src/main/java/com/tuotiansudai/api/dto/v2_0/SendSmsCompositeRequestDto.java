package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.enums.SmsCaptchaType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SendSmsCompositeRequestDto extends BaseParamDto {
    @NotNull(message = "0022")
    @ApiModelProperty(value = "验证码类型", example = "REGISTER_CAPTCHA,RETRIEVE_PASSWORD_CAPTCHA,TURN_OFF_NO_PASSWORD_INVEST")
    private SmsCaptchaType type;

    @NotEmpty(message = "0001")
    @Pattern(regexp = "^1\\d{10}$", message = "0002")
    @ApiModelProperty(value = "手机号", example = "15900000001")
    private String phoneNum;

    @ApiModelProperty(value = "图形验证码", example = "123456")
    private String imageCaptcha;

    @ApiModelProperty(value = "是否语音验证码", example = "true")
    private boolean isVoice;

    public SmsCaptchaType getType() {
        return type;
    }

    public void setType(SmsCaptchaType type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getImageCaptcha() {
        return imageCaptcha;
    }

    public void setImageCaptcha(String imageCaptcha) {
        this.imageCaptcha = imageCaptcha;
    }

    public boolean getVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }
}

