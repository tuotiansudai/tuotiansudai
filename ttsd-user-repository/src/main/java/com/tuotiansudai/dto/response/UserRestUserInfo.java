package com.tuotiansudai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.UserModel;

public class UserRestUserInfo extends UserRestResponseBase {
    @JsonProperty("user_info")
    private UserModel userInfo;

    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }
}
