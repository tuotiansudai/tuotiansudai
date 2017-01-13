package com.tuotiansudai.ask.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class QuestionWithCaptchaRequestDto extends QuestionRequestDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^[0-9]{5}$")
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
