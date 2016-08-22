package com.tuotiansudai.ask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionResultDataDto extends BaseDataDto {

    private boolean isCaptchaValid;

    private boolean isQuestionSensitiveValid;

    private boolean isAdditionSensitiveValid;

    @JsonProperty(value = "isCaptchaValid")
    public boolean isCaptchaValid() {
        return isCaptchaValid;
    }

    public void setCaptchaValid(boolean captchaValid) {
        isCaptchaValid = captchaValid;
    }

    @JsonProperty(value = "isQuestionSensitiveValid")
    public boolean isQuestionSensitiveValid() {
        return isQuestionSensitiveValid;
    }

    public void setQuestionSensitiveValid(boolean questionSensitiveValid) {
        isQuestionSensitiveValid = questionSensitiveValid;
    }

    @JsonProperty(value = "isAdditionSensitiveValid")
    public boolean isAdditionSensitiveValid() {
        return isAdditionSensitiveValid;
    }

    public void setAdditionSensitiveValid(boolean additionSensitiveValid) {
        isAdditionSensitiveValid = additionSensitiveValid;
    }
}
