package com.tuotiansudai.dto.ranking;

public class UserScoreDto {

    private String loginName;

    private double score;

    public UserScoreDto(String loginName, double score){
        this.loginName = loginName;
        this.score = score;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
