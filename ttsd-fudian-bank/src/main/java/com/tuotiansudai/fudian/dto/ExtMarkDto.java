package com.tuotiansudai.fudian.dto;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class ExtMarkDto {

    private String loginName;

    private String mobile;

    private ApiType apiType;

    private Map<String, String> extraValues;

    public ExtMarkDto() {
    }

    public ExtMarkDto(String loginName, String mobile, ApiType apiType, Map<String, String> extraValues) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.apiType = apiType;
        this.extraValues = extraValues;
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

    public ApiType getApiType() {
        return apiType;
    }

    public void setApiType(ApiType apiType) {
        this.apiType = apiType;
    }

    public Map<String, String> getExtraValues() {
        return extraValues;
    }

    public void setExtraValues(Map<String, String> extraValues) {
        this.extraValues = extraValues;
    }
}
