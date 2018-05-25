package com.tuotiansudai.activity.repository.model;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class SuperScholarRewardModel implements Serializable{

    private long id;
    private String loginName;
    private String questionIndex;
    private String questionAnswer;
    private String userAnswer;
    private int userRight;
    private Long couponId;
    private boolean isShareHome;
    private boolean isShareAccount;
    private boolean isShareInvest;
    private boolean isCashBack;
    private Date createdTime;
    private Date answerTime;
    private Date updatedTime;


    public SuperScholarRewardModel() {
    }

    public SuperScholarRewardModel(String loginName, String questionIndex, String questionAnswer) {
        this.loginName = loginName;
        this.questionIndex = questionIndex;
        this.questionAnswer = questionAnswer;
        this.createdTime = new Date();
        this.updatedTime = new Date();
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

    public String getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(String questionIndex) {
        this.questionIndex = questionIndex;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getUserRight() {
        return userRight;
    }

    public void setUserRight(int userRight) {
        this.userRight = userRight;
    }

    public boolean isShareHome() {
        return isShareHome;
    }

    public void setShareHome(boolean shareHome) {
        isShareHome = shareHome;
    }

    public boolean isShareAccount() {
        return isShareAccount;
    }

    public void setShareAccount(boolean shareAccount) {
        isShareAccount = shareAccount;
    }

    public boolean isShareInvest() {
        return isShareInvest;
    }

    public void setShareInvest(boolean shareInvest) {
        isShareInvest = shareInvest;
    }

    public boolean isCashBack() {
        return isCashBack;
    }

    public void setCashBack(boolean cashBack) {
        isCashBack = cashBack;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public double getRewardRate(){
        return answerRate.get(this.userRight)
                + (this.isShareHome ? 0.001D : 0D)
                + (this.isShareAccount ? 0.002D : 0D)
                + (this.isShareInvest ? 0.006D : 0D);
    }

    private Map<Integer, Double> answerRate = Maps.newHashMap(ImmutableMap.<Integer, Double>builder()
            .put(0, 0D)
            .put(1, 0.002D)
            .put(2, 0.003D)
            .put(3, 0.004D)
            .put(4, 0.005D)
            .put(5, 0.006D)
            .build());
}
