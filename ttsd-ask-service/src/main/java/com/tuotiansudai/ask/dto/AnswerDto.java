package com.tuotiansudai.ask.dto;

import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.AnswerStatus;
import com.tuotiansudai.ask.utils.SensitiveWordsFilter;

import java.util.Date;

public class AnswerDto {

    private QuestionDto question;

    private long id;

    private String mobile;

    private String answer;

    private boolean bestAnswer;

    private int favorite;

    private boolean favored;

    private String approvedBy;

    private Date approvedTime;

    private String rejectedBy;

    private Date rejectedTime;

    private AnswerStatus status;

    private Date createdTime;

    public AnswerDto(AnswerModel answerModel, String mobile, boolean isFavored, QuestionDto questionDto) {
        this.question = questionDto;
        this.id = answerModel.getId();
        this.mobile = mobile;
        this.answer = SensitiveWordsFilter.filter(answerModel.getAnswer());
        this.bestAnswer = answerModel.isBestAnswer();
        this.favorite = answerModel.getFavoredBy() != null ? answerModel.getFavoredBy().size() : 0;
        this.favored = isFavored;
        this.approvedBy = answerModel.getApprovedBy();
        this.approvedTime = answerModel.getApprovedTime();
        this.rejectedBy = answerModel.getRejectedBy();
        this.rejectedTime = answerModel.getRejectedTime();
        this.status = answerModel.getStatus();
        this.createdTime = answerModel.getCreatedTime();
    }

    public QuestionDto getQuestion() {
        return question;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public Date getRejectedTime() {
        return rejectedTime;
    }

    public AnswerStatus getStatus() {
        return status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
