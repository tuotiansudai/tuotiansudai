package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class FundManagementRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "用户ID", dataType = "test")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
