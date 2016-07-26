package com.tuotiansudai.api.dto.v1_0;


public class MobileAppUnreadMessageCount extends BaseResponseDataDto {
    private long unreadMessageCount;
    private boolean newMessage;

    public long getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(long unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }
}
