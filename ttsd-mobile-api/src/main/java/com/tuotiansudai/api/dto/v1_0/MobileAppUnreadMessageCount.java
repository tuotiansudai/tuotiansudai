package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class MobileAppUnreadMessageCount extends BaseResponseDataDto {

    @ApiModelProperty(value = "未读数", example = "10")
    private long unreadMessageCount;

    @ApiModelProperty(value = "是否有新消息", example = "true")
    private boolean newMessage;

    public MobileAppUnreadMessageCount(long unreadMessageCount, boolean newMessage) {
        this.unreadMessageCount = unreadMessageCount;
        this.newMessage = newMessage;
    }

    public long getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public boolean isNewMessage() {
        return newMessage;
    }
}
