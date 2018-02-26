package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;

public class AgreementDto implements Serializable {

    private String loginName;

    private boolean fastPay;

    private boolean autoRepay;

    private boolean noPasswordInvest;

    private boolean huizuAutoRepay;

    private Source source = Source.WEB;

    private String ip;

    private String deviceId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isAutoRepay() {
        return autoRepay;
    }

    public void setAutoRepay(boolean autoRepay) {
        this.autoRepay = autoRepay;
    }

    public boolean isNoPasswordInvest() {
        return noPasswordInvest;
    }

    public void setNoPasswordInvest(boolean noPasswordInvest) {
        this.noPasswordInvest = noPasswordInvest;
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

    public boolean isHuizuAutoRepay() {
        return huizuAutoRepay;
    }

    public void setHuizuAutoRepay(boolean huizuAutoRepay) {
        this.huizuAutoRepay = huizuAutoRepay;
    }
}
