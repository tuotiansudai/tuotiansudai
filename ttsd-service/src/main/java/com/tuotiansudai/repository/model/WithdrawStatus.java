package com.tuotiansudai.repository.model;

public enum WithdrawStatus {
    WAIT_VERIFY("等待初审"),
    VERIFY_FAIL("初审失败"),
    RECHECK("等待复审"),
    RECHECK_FAIL("复审失败"),
    SUCCESS("提现成功");

    private final String description;

    WithdrawStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
