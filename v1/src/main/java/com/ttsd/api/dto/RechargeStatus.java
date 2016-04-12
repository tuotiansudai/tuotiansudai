package com.ttsd.api.dto;

import com.esoft.archer.user.UserConstants;

public enum RechargeStatus {
    WAIT_PAY(UserConstants.RechargeStatus.WAIT_PAY, "等待付款"),
    SUCCESS(UserConstants.RechargeStatus.SUCCESS, "充值成功"),
    FAIL(UserConstants.RechargeStatus.FAIL, "充值失败");
    String code;
    String message;

    RechargeStatus(String code, String message) {
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
        for (RechargeStatus status : RechargeStatus.values()) {
            if (status.code.equals(code)) {
                return status.message;
            }
        }
        return null;
    }
}
