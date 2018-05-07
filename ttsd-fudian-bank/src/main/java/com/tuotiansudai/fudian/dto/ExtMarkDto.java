package com.tuotiansudai.fudian.dto;

import com.tuotiansudai.fudian.config.ApiType;

public class ExtMarkDto {

    private String loginName;

    private String mobile;

    private ApiType apiType;

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

    public ApiType getApiType() {
        return apiType;
    }

    public void setApiType(ApiType apiType) {
        this.apiType = apiType;
    }
}
