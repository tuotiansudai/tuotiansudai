package com.tuotiansudai.repository.model;

public enum WithdrawStatus {
    WAIT_PAY("等待支付"), //WAIT_VERIFY("等待初审")
    APPLY_SUCCESS("申请成功"), //RECHECK("等待复审")
    APPLY_FAIL("申请失败"), //VERIFY_FAIL("初审失败")
    SUCCESS("提现成功"),
    FAIL("提现失败"); // RECHECK_FAIL("复审失败")

    private final String description;

    WithdrawStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
