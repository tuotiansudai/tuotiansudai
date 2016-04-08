package com.tuotiansudai.dto.ranking;

public class UserScoreDto {

    private String loginName;

    private long score;

    public UserScoreDto(String loginName, long score){
        this.loginName = loginName;
        this.score = score;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
