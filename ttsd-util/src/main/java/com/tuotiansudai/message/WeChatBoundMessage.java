package com.tuotiansudai.message;

import java.io.Serializable;

public class WeChatBoundMessage implements Serializable {
    private String mobile;

    private String openid;

    public WeChatBoundMessage() {
    }

    public WeChatBoundMessage(String mobile, String openid) {
        this.mobile = mobile;
        this.openid = openid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
