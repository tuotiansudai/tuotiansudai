package com.tuotiansudai.ask.repository.dto;

import com.tuotiansudai.ask.repository.model.QuestionModel;

import java.io.Serializable;

public class EmbodyQuestionDto implements Serializable {

    private long id;

    private String question;

    private String linkUrl;

    private static final String SITE_MAP = "http://ask.tuotiansudai.com/question/";

    public EmbodyQuestionDto(QuestionModel questionModel) {
        this.id = questionModel.getId();
        this.question = questionModel.getQuestion();
        this.linkUrl = SITE_MAP + questionModel.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
