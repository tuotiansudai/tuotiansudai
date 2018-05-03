package com.tuotiansudai.enums;

public enum WeChatMessageType {
    TRANSFER_SUCCESS("transfer_wechat_message_request_body.json"),

    WITHDRAW_NOTIFY_SUCCESS("withdraw_notify_wechat_message_request_body.json"),

    WITHDRAW_APPLY_SUCCESS("withdraw_apply_wechat_message_request_body.json"),

    ADVANCE_REPAY_SUCCESS("advance_repay_wechat_message_request_body.json"),

    NORMAL_REPAY_SUCCESS("normal_repay_wechat_message_request_body.json"),

    LOAN_OUT_SUCCESS("loan_out_wechat_message_request_body.json"),

    INVEST_SUCCESS("invest_wechat_message_request_body.json"),

    LOAN_COMPLETE("loan_complete_wechat_message_request_body.json"),

    BOUND_TO_OTHER_USER("bound_wechat_message_request_body.json"); //账户被其他微信号绑定消息


    private final String jsonFile;

    WeChatMessageType(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public String getJsonFile() {
        return jsonFile;
    }
}
