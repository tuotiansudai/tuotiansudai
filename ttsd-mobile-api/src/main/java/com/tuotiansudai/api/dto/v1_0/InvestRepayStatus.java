package com.tuotiansudai.api.dto.v1_0;

public enum InvestRepayStatus {
    REPAYING("repaying","还款中"),
    WAIT_REPAY_VERIFY("wait_repay_verify","等待还款确认"),
    OVERDUE("overdue","逾期"),
    COMPLETE("complete","完成"),
    BAD_DEBT("bad_debt","坏账");
    String code;
    String message;

    InvestRepayStatus(String code, String message) {
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
        for (InvestRepayStatus status : InvestRepayStatus.values()) {
            if (status.code.equals(code)) {
                return status.message;
            }
        }
        return null;
    }
}
