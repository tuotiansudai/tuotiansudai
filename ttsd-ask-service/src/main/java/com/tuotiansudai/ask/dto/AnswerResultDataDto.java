package com.tuotiansudai.ask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerResultDataDto extends BaseDataDto {

    private boolean isCaptchaValid;

    private boolean isAnswerSensitiveValid;

    @JsonProperty(value = "isCaptchaValid")
    public boolean isCaptchaValid() {
        return isCaptchaValid;
    }

    public void setCaptchaValid(boolean captchaValid) {
        isCaptchaValid = captchaValid;
    }

    @JsonProperty(value = "isAnswerSensitiveValid")
    public boolean isAnswerSensitiveValid() {
        return isAnswerSensitiveValid;
    }

    public void setAnswerSensitiveValid(boolean answerSensitiveValid) {
        isAnswerSensitiveValid = answerSensitiveValid;
    }
}
