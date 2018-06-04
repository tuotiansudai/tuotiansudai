package com.tuotiansudai.fudian.dto;

public class ExtMarkDto {

    private String loginName;

    private String mobile;

    public ExtMarkDto() {
    }

    public ExtMarkDto(String loginName, String mobile) {
        this.loginName = loginName;
        this.mobile = mobile;
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
}
