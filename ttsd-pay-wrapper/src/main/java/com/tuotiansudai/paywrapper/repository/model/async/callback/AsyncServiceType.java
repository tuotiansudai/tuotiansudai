package com.tuotiansudai.paywrapper.repository.model.async.callback;

public enum AsyncServiceType {
    MER_BIND_CARD_NOTIFY("mer_bind_card_notify","绑卡换卡结果后台通知商户");

    AsyncServiceType(String code, String description){
        this.code = code;
        this.description = description;
    }
    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
