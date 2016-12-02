package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class UserBillDetailListRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "用户名", example = "wangtuotian")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
