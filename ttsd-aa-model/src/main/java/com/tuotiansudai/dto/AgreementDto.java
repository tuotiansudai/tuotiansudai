package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;

public class AgreementDto implements Serializable {

    private String loginName;

    private boolean noPasswordInvest;

    private Source source = Source.WEB;

    private String ip;

    private String deviceId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isNoPasswordInvest() {
        return noPasswordInvest;
    }

    public void setNoPasswordInvest(boolean noPasswordInvest) {
        this.noPasswordInvest = noPasswordInvest;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
