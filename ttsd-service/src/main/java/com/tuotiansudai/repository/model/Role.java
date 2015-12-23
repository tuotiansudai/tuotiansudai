package com.tuotiansudai.repository.model;

public enum Role {
    USER("注册用户"),
    INVESTOR("出借人"),
    LOANER("借款人"),
    STAFF("业务员"),
    CUSTOMER_SERVICE("客服"),
    AGENT("代理商"),
    ADMIN("管理员");

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}