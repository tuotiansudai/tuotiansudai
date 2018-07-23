package com.tuotiansudai.fudian.umpmessage;


import com.tuotiansudai.fudian.message.BankBaseMessage;

public class UmpBindCardMessage extends BankBaseMessage {

    private long bindCardModelId;

    private String loginName;

    private String bankCode;

    private boolean isApply;

    private boolean isReplaceCard;

    public UmpBindCardMessage() {
    }

    public UmpBindCardMessage(long bindCardModelId, String loginName, boolean isReplaceCard) {
        this.bindCardModelId = bindCardModelId;
        this.loginName = loginName;
        this.isReplaceCard = isReplaceCard;
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

    public boolean isApply() {
        return isApply;
    }

    public void setApply(boolean apply) {
        isApply = apply;
    }

    public boolean isReplaceCard() {
        return isReplaceCard;
    }

    public void setReplaceCard(boolean replaceCard) {
        isReplaceCard = replaceCard;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
