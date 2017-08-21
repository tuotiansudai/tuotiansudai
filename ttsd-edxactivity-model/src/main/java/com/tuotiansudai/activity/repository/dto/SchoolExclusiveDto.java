package com.tuotiansudai.activity.repository.dto;

public class SchoolExclusiveDto {

    private String loginName;
    private int JDECard;

    public SchoolExclusiveDto() {
    }

    public SchoolExclusiveDto(String loginName, int JDECard) {
        this.loginName = loginName;
        this.JDECard = JDECard;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getJDECard() {
        return JDECard;
    }

    public void setJDECard(int JDECard) {
        this.JDECard = JDECard;
    }
}
