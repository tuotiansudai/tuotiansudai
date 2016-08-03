package com.tuotiansudai.ask.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QuestionModel implements Serializable {

    private long id;

    private String loginName;

    private String mobile;

    private String question;

    private String addition;

    private List<Tag> tags;

    private int answers;

    private Date lastAnsweredTime;

    private boolean approved;

    private String approvedBy;

    private Date approvedTime;

    private Date createdTime;

    public QuestionModel() {
    }

    public QuestionModel(String loginName, String question, String addition, List<Tag> tags) {
        this.loginName = loginName;
        this.question = question;
        this.addition = addition;
        this.tags = tags;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public Date getLastAnsweredTime() {
        return lastAnsweredTime;
    }

    public void setLastAnsweredTime(Date lastAnsweredTime) {
        this.lastAnsweredTime = lastAnsweredTime;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
