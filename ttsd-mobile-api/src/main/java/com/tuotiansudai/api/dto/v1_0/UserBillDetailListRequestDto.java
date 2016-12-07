package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class UserBillDetailListRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "用户名", example = "wangtuotian")
    private String userId;
    private UserBillCategory userBillCategory;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserBillCategory getUserBillCategory() {
        return userBillCategory;
    }

    public void setUserBillCategory(UserBillCategory userBillCategory) {
        this.userBillCategory = userBillCategory;
    }
}
