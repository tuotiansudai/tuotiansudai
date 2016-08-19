package com.tuotiansudai.ask.dto;

import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.utils.SensitiveWordsFilter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QuestionDto implements Serializable {

    private long id;

    private String question;

    private String addition;

    private int answers;

    private String mobile;

    private List<Tag> tags;

    private String approvedBy;

    private Date approvedTime;

    private String rejectedBy;

    private Date rejectedTime;

    private QuestionStatus status;

    private Date createdTime;

    public QuestionDto(QuestionModel questionModel, String mobile) {
        this.id = questionModel.getId();
        this.question = SensitiveWordsFilter.filter(questionModel.getQuestion());
        this.addition = SensitiveWordsFilter.filter(questionModel.getAddition());
        this.answers = questionModel.getAnswers();
        this.mobile = mobile;
        this.tags = questionModel.getTags();
        this.approvedBy = questionModel.getApprovedBy();
        this.approvedTime = questionModel.getApprovedTime();
        this.rejectedBy = questionModel.getRejectedBy();
        this.rejectedTime = questionModel.getRejectedTime();
        this.status = questionModel.getStatus();
        this.createdTime = questionModel.getCreatedTime();
    }

    public long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAddition() {
        return addition;
    }

    public int getAnswers() {
        return answers;
    }

    public String getMobile() {
        return mobile;
    }

    public List<Tag> getTags() {
        return tags;
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

    public QuestionStatus getStatus() {
        return status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
