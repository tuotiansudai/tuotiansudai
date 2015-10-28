package com.tuotiansudai.repository.model;

public enum BankCardStatus {

    PASSED("通过"),
    UNCHECK("未审核"),
    REMOVE("移除"),
    FAIL("失败");
    private final String status;

    BankCardStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
