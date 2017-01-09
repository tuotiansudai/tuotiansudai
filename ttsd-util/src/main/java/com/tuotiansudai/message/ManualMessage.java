package com.tuotiansudai.message;

import java.io.Serializable;
import java.util.List;

public class ManualMessage implements Serializable {

    private String loginName;

    private List<Long> messageIds;

    public ManualMessage() {
    }

    public ManualMessage(String loginName, List<Long> messageIds) {
        this.loginName = loginName;
        this.messageIds = messageIds;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Long> messageIds) {
        this.messageIds = messageIds;
    }
}
