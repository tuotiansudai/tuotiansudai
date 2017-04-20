package com.tuotiansudai.enums;

public enum  WeChatMessageType {

    BOUND_TO_OTHER_USER("wechat_message_request_body.json") //账户被其他微信号绑定消息
    ;

    private final String jsonFile;

    WeChatMessageType(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public String getJsonFile() {
        return jsonFile;
    }
}
