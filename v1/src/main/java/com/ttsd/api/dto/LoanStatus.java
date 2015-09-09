package com.ttsd.api.dto;

import com.esoft.jdp2p.loan.LoanConstants;

public enum LoanStatus {
    WAITING_VERIFY(LoanConstants.LoanStatus.WAITING_VERIFY,"等待审核"),
    WAITING_VERIFY_AFFIRM(LoanConstants.LoanStatus.WAITING_VERIFY_AFFIRM,"审核后等待第三方确认"),
    WAITING_VERIFY_AFFIRM_USER(LoanConstants.LoanStatus.WAITING_VERIFY_AFFIRM_USER,"审核后等待用户确认"),
    DQGS(LoanConstants.LoanStatus.DQGS,"贷前公示"),
    VERIFY_FAIL(LoanConstants.LoanStatus.VERIFY_FAIL,"审核未通过"),
    RAISING(LoanConstants.LoanStatus.RAISING,"筹款中"),
    RECHECK(LoanConstants.LoanStatus.RECHECK,"等待复核"),
    WAITING_RECHECK_VERIFY(LoanConstants.LoanStatus.WAITING_RECHECK_VERIFY,"放款后，等待确认"),
    CANCEL(LoanConstants.LoanStatus.CANCEL,"流标"),
    WAITING_CANCEL_AFFIRM(LoanConstants.LoanStatus.WAITING_CANCEL_AFFIRM,"流标后等待第三方确认"),
    REPAYING(LoanConstants.LoanStatus.REPAYING,"还款中"),
    WAIT_REPAY_VERIFY(LoanConstants.LoanStatus.WAIT_REPAY_VERIFY,"等待还款确认"),
    OVERDUE(LoanConstants.LoanStatus.OVERDUE,"逾期"),
    COMPLETE(LoanConstants.LoanStatus.COMPLETE,"完成"),
    BAD_DEBT(LoanConstants.LoanStatus.BAD_DEBT,"坏账");

    String code;
    String message;

    LoanStatus(String code, String message) {
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
        for (LoanStatus status : LoanStatus.values()) {
            if (status.code.equals(code)) {
                return status.message;
            }
        }
        return null;
    }
}
