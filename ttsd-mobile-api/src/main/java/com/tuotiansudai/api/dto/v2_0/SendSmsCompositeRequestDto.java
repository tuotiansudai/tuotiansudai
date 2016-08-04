package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.CaptchaType;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SendSmsCompositeRequestDto extends BaseParamDto {
    @NotNull(message = "0022")
    private CaptchaType type;

    @NotEmpty(message = "0001")
    @Pattern(regexp = "^1\\d{10}$", message = "0002")
    private String phoneNum;

    private String imageCaptcha;

    public CaptchaType getType() {
        return type;
    }

    public void setType(CaptchaType type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getImageCaptcha() { return imageCaptcha; }

    public void setImageCaptcha(String imageCaptcha) { this.imageCaptcha = imageCaptcha; }
}

