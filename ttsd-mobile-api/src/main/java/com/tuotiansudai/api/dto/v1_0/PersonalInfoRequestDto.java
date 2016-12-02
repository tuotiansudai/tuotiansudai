package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class PersonalInfoRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "用户名称", example = "wangtuotian")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
