package com.ttsd.api.dto;

import com.esoft.archer.user.UserConstants;

public enum WithdrawStatus {
    RECHECK(UserConstants.WithdrawStatus.RECHECK, "等待复核"),
    SUCCESS(UserConstants.WithdrawStatus.SUCCESS, "提现成功"),
    WAIT_VERIFY(UserConstants.WithdrawStatus.WAIT_VERIFY, "等待审核"),
    VERIFY_FAIL(UserConstants.WithdrawStatus.VERIFY_FAIL, "审核未通过"),
    RECHECK_FAIL(UserConstants.WithdrawStatus.RECHECK_FAIL, "复核未通过");

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
