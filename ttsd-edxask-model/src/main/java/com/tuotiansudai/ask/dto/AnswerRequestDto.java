package com.tuotiansudai.ask.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class AnswerRequestDto implements Serializable {

    private long questionId;

    @NotEmpty
    @Pattern(regexp = "^.{10,10000}$")
    private String answer;

    @NotEmpty
    @Pattern(regexp = "^(\\d+){5}$")
    private String captcha;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
