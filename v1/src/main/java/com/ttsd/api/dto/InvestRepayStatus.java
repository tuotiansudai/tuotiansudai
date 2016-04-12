package com.ttsd.api.dto;

import com.esoft.jdp2p.loan.LoanConstants;

public enum InvestRepayStatus {
    REPAYING(LoanConstants.RepayStatus.REPAYING, "还款中"),
    WAIT_REPAY_VERIFY(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY, "等待还款确认"),
    OVERDUE(LoanConstants.RepayStatus.OVERDUE, "逾期"),
    COMPLETE(LoanConstants.RepayStatus.COMPLETE, "完成"),
    BAD_DEBT(LoanConstants.RepayStatus.BAD_DEBT, "坏账");
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
