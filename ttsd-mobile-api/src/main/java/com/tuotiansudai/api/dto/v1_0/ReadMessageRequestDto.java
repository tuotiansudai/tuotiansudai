package com.tuotiansudai.api.dto.v1_0;


public class ReadMessageRequestDto extends BaseParamDto {
    private String userMessageId;

    public String getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(String userMessageId) {
        this.userMessageId = userMessageId;
    }
}
