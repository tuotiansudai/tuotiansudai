package com.tuotiansudai.ask.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IncludeQuestionModel implements Serializable {

    private long id;

    private long questionId;

    private String questionTitle;

    private String questionLink;

    private List<Tag> tags;

    private Date createdTime;

    public IncludeQuestionModel() {
    }

    public IncludeQuestionModel(long questionId, String questionTitle, String questionLink,  List<Tag> tags) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionLink = questionLink;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionLink() {
        return questionLink;
    }

    public void setQuestionLink(String questionLink) {
        this.questionLink = questionLink;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
