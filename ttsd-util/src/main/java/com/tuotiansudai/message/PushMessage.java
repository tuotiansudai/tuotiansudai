package com.tuotiansudai.message;

import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;

import java.io.Serializable;
import java.util.List;

public class PushMessage implements Serializable {

    private List<String> loginNames;

    private PushSource pushSource;

    private PushType pushType;

    private String content;

    public PushMessage() {
    }

    public PushMessage(List<String> loginNames, PushSource pushSource, PushType pushType, String content) {
        this.loginNames = loginNames;
        this.pushSource = pushSource;
        this.pushType = pushType;
        this.content = content;
    }

    public List<String> getLoginNames() {
        return loginNames;
    }

    public void setLoginNames(List<String> loginNames) {
        this.loginNames = loginNames;
    }

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
