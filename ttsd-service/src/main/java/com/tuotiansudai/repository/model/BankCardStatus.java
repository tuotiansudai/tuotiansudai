package com.tuotiansudai.repository.model;

public enum BankCardStatus {

    PASSED("通过"),

    UNCHECKED("未审核"),

    REMOVED("移除"),

    FAILED("失败");

    private final String status;

    BankCardStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
