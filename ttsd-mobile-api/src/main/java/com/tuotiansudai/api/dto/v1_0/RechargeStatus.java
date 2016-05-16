package com.tuotiansudai.api.dto.v1_0;

public enum RechargeStatus {
    WAIT_PAY("wait_pay", "等待付款"),
    SUCCESS("success", "充值成功"),
    FAIL("fail", "充值失败");
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
