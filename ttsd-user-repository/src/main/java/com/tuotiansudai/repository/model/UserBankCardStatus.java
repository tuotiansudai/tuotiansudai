package com.tuotiansudai.repository.model;

public enum UserBankCardStatus {

    BOUND("已绑定"),

    UNBOUND("已解绑");

    private final String status;

    UserBankCardStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
