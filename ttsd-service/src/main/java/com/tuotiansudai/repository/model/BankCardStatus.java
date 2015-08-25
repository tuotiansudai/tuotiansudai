package com.tuotiansudai.repository.model;

public enum BankCardStatus {

    PASSED("passed"),
    UNCHECK("uncheck"),
    REMOVE("remove"),
    FAIL("fail");
    private final String status;

    BankCardStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
