package com.ttsd.api.dto;

import com.esoft.jdp2p.invest.InvestConstants;


public enum InvestStatus {
    WAIT_AFFIRM(InvestConstants.InvestStatus.WAIT_AFFIRM, "等待付款"),
    BID_SUCCESS(InvestConstants.InvestStatus.BID_SUCCESS, "投资成功"),
    WAIT_LOANING_VERIFY(InvestConstants.InvestStatus.WAIT_LOANING_VERIFY, "放款后等待第三方确认"),
    CANCEL(InvestConstants.InvestStatus.CANCEL, "流标"),
    REPAYING(InvestConstants.InvestStatus.REPAYING, "还款中"),
    OVERDUE(InvestConstants.InvestStatus.OVERDUE, "逾期"),
    COMPLETE(InvestConstants.InvestStatus.COMPLETE, "已完成"),
    BAD_DEBT(InvestConstants.InvestStatus.BAD_DEBT, "坏账"),
    UNFINISHED(InvestConstants.InvestStatus.UNFINISHED, "已取消"),
    TEST(InvestConstants.InvestStatus.TEST, "内部测试");

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
}
