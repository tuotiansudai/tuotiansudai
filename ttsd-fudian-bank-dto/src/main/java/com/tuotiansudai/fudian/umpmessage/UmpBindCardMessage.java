package com.tuotiansudai.fudian.umpmessage;


import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpBindCardMessage extends BankBaseMessage {

    private long bindCardModelId;

    private String loginName;

    private boolean isApply;

    private boolean isFastPayOn;

    public UmpBindCardMessage() {
    }

    public UmpBindCardMessage(long bindCardModelId, String loginName) {
        this.bindCardModelId = bindCardModelId;
        this.loginName = loginName;
    }

    public long getBindCardModelId() {
        return bindCardModelId;
    }

    public void setBindCardModelId(long bindCardModelId) {
        this.bindCardModelId = bindCardModelId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isFastPayOn() {
        return isFastPayOn;
    }

    public void setFastPayOn(boolean fastPayOn) {
        isFastPayOn = fastPayOn;
    }

    public boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }
}
