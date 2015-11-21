package com.tuotiansudai.api.dto;

public enum InvestStatus {
    WAIT_AFFIRM("wait_affirm", "等待付款"),
    BID_SUCCESS("bid_success", "投资成功"),
    WAIT_LOANING_VERIFY("wait_loaning_verify", "放款后等待第三方确认"),
    CANCEL("cancel", "流标"),
    REPAYING("repaying", "还款中"),
    OVERDUE("overdue", "逾期"),
    COMPLETE("complete", "已完成"),
    BAD_DEBT("bad_debt", "坏账"),
    UNFINISHED("unfinished", "已取消"),
    TEST("test", "内部测试");

    String code;
    String message;

    InvestStatus(String code, String message) {
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
        for (InvestStatus status : InvestStatus.values()) {
            if (status.code.equals(code)) {
                return status.message;
            }
        }
        return null;
    }

    public static InvestStatus convertInvestStatus(com.tuotiansudai.repository.model.InvestStatus investStatus) {
        if (investStatus == com.tuotiansudai.repository.model.InvestStatus.SUCCESS) {
            return BID_SUCCESS;
        }
        if (investStatus == com.tuotiansudai.repository.model.InvestStatus.WAIT_PAY) {
            return WAIT_AFFIRM;
        }
        return UNFINISHED;
    }
}
