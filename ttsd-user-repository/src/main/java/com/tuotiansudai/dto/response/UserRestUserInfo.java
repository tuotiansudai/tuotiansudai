package com.tuotiansudai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRestUserInfo extends UserRestResponseBase {
    @JsonProperty("user_info")
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
