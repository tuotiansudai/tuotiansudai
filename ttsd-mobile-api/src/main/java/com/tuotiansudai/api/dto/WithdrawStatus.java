package com.tuotiansudai.api.dto;

public enum WithdrawStatus {
    RECHECK("recheck", "等待复核"),
    SUCCESS("success", "提现成功"),
    WAIT_VERIFY("wait_verify", "等待审核"),
    VERIFY_FAIL("verify_fail", "审核未通过"),
    RECHECK_FAIL("recheck_fail", "复核未通过");


    String code;
    String message;

    WithdrawStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        for (WithdrawStatus status : WithdrawStatus.values()) {
            if (status.code.equals(code)) {
                return status.message;
            }
        }
        return null;
    }
}
