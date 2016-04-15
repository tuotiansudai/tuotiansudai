package com.tuotiansudai.dto.ranking;

public class PrizeWinnerDto {

    private String loginName;

    private String realName;

    private String mobile;

    private String identityNumber;

    private String time;

    public PrizeWinnerDto(String loginName, String realName, String mobile, String identityNumber, String time) {
        this.loginName = loginName;
        this.realName = realName;
        this.mobile = mobile;
        this.identityNumber = identityNumber;
        this.time = time;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
