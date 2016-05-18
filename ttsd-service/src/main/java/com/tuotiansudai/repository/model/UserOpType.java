package com.tuotiansudai.repository.model;


public enum UserOpType {

    CHANGE_PASSWORD("修改密码"),
    BIND_CHANGE_EMAIL("绑定/修改邮箱"),
    BIND_CARD("绑卡"),
    REPLACE_CARD("换卡"),
    FAST_PAY_AGREEMENT("开通快捷支付协议"),
    NO_PASSWORD_AGREEMENT("开通免密支付协议"),
    AUTO_INVEST("自动投标"),
    INVEST_NO_PASSWORD("免密投资");

    private String desc;

    UserOpType(String desc) {
        this.desc = desc;
    }
}
