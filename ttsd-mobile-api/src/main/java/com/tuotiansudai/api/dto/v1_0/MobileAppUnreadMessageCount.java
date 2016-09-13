package com.tuotiansudai.api.dto.v1_0;


public class MobileAppUnreadMessageCount extends BaseResponseDataDto {

    private long unreadMessageCount;

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
