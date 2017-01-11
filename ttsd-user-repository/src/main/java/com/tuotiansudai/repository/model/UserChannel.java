package com.tuotiansudai.repository.model;



public enum UserChannel {
    WX_FRIEND("微信好友"),
    WX_ZONE("微信朋友圈"),
    QQ_FRIEND("QQ好友"),
    Q_ZONE("QQ空间"),
    SMS("短信");

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    UserChannel(String description){
        this.description = description;
    }
}
