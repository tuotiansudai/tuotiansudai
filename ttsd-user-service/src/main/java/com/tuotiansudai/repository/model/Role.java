package com.tuotiansudai.repository.model;

public enum Role {
    USER("注册用户"),
    INVESTOR("出借人"),
    LOANER("借款人"),
    STAFF("业务员"),
    OPERATOR("运营专员"),
    OPERATOR_ADMIN("运营管理员"),
    CUSTOMER_SERVICE("客服"),
    AGENT("代理商"),
    ADMIN("管理员"),
    EDITOR("编辑"),
    DATA("数据专员"),
    ASK_ADMIN("问答管理员");

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}