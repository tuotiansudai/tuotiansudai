package com.tuotiansudai.message;

import java.io.Serializable;

public class ObtainPointMessage implements Serializable {

    private String loginName;
    private long point;

    public ObtainPointMessage() {
    }

    public ObtainPointMessage(String loginName, long point) {
        this.loginName = loginName;
        this.point = point;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}
