package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;

public class AgreementDto implements Serializable {

    private String loginName;

    private boolean autoInvest;

    private boolean fastPay;

    private boolean autoRepay;

    private boolean noPasswordInvest;

    private Source source = Source.WEB;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
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

}
