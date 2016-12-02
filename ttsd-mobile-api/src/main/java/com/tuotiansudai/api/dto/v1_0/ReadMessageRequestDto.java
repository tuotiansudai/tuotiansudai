package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class ReadMessageRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "消息ID", example = "10")
    private String userMessageId;

    public String getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(String userMessageId) {
        this.userMessageId = userMessageId;
    }
}
