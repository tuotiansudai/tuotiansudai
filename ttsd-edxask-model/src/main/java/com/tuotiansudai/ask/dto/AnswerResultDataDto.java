package com.tuotiansudai.ask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.dto.BaseDataDto;

public class AnswerResultDataDto extends BaseDataDto {

    private boolean isCaptchaValid;

    private boolean isAnswerSensitiveValid;

    private String sensitiveWord;

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

    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }
}
