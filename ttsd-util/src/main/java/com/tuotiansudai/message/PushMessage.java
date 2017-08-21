package com.tuotiansudai.message;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PushMessage implements Serializable {

    private List<String> loginNames;

    private PushSource pushSource;

    private PushType pushType;

    private String content;

    private AppUrl jumpTo;

    private Map<String, String> params;


    public PushMessage() {
    }

    public PushMessage(List<String> loginNames, PushSource pushSource, PushType pushType, String content, AppUrl jumpTo, Map<String, String> params) {
        this.loginNames = loginNames;
        this.pushSource = pushSource;
        this.pushType = pushType;
        this.content = content;
        this.jumpTo = jumpTo;
        this.params = params;
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

    public AppUrl getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(AppUrl jumpTo) {
        this.jumpTo = jumpTo;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
