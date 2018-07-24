package com.tuotiansudai.enums;

public enum WithdrawStatus {
    WAIT_PAY("等待支付"), //WAIT_VERIFY("等待初审")
    SUCCESS("提现成功"),
    FAIL("提现失败"), // RECHECK_FAIL("复审失败")

    UMP_APPLY_SUCCESS("申请成功,处理中"),
    UMP_APPLY_FAIL("申请失败");

    private final String description;

    WithdrawStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
