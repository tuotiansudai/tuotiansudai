package com.tuotiansudai.ask.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AnswerModel implements Serializable {

    private long id;

    private String loginName;

    private long questionId;

    private String answer;

    private boolean bestAnswer;

    private List<String> favoredBy;

    private String approvedBy;

    private Date approvedTime;

    private String rejectedBy;

    private Date rejectedTime;

    private AnswerStatus status;

    private Date createdTime;

    public AnswerModel() {
    }

    public AnswerModel(String loginName, long questionId, String answer) {
        this.loginName = loginName;
        this.questionId = questionId;
        this.answer = answer;
        this.status = AnswerStatus.UNAPPROVED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

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

    public boolean isBestAnswer() {
        return bestAnswer;
    }

    public void setBestAnswer(boolean bestAnswer) {
        this.bestAnswer = bestAnswer;
    }

    public List<String> getFavoredBy() {
        return favoredBy;
    }

    public void setFavoredBy(List<String> favoredBy) {
        this.favoredBy = favoredBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(String rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public Date getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(Date rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public AnswerStatus getStatus() {
        return status;
    }

    public void setStatus(AnswerStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
