package com.tuotiansudai.ask.dto;

import com.tuotiansudai.ask.repository.model.AnswerModel;

import java.util.Date;

public class AnswerDto {

    private long id;

    private String mobile;

    private String answer;

    private boolean bestAnswer;

    private int favorite;

    private boolean favored;

    private Date createdTime;

    public AnswerDto(AnswerModel answerModel, String mobile, boolean isFavored) {
        this.id = answerModel.getId();
        this.mobile = mobile;
        this.answer = answerModel.getAnswer();
        this.bestAnswer = answerModel.isBestAnswer();
        this.favorite = answerModel.getFavoredBy().size();
        this.favored = isFavored;
        this.createdTime = answerModel.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isBestAnswer() {
        return bestAnswer;
    }

    public int getFavorite() {
        return favorite;
    }

    public boolean getFavored() {
        return favored;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
