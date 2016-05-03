package com.tuotiansudai.api.dto.v1_0;

public enum LoanStatus {
    WAITING_VERIFY("waiting_verify", "等待审核"),
    WAITING_VERIFY_AFFIRM("waiting_verify_affirm", "审核后等待第三方确认"),
    WAITING_VERIFY_AFFIRM_USER("waiting_verify_affirm_user", "审核后等待用户确认"),
    DQGS("dqgs", "贷前公示"),
    PREHEAT("preheat", "预热"),
    VERIFY_FAIL("verify_fail", "审核未通过"),
    RAISING("raising", "筹款中"),
    RECHECK("recheck", "等待复核"),
    WAITING_RECHECK_VERIFY("waiting_recheck_verify", "放款后，等待确认"),
    CANCEL("cancel", "流标"),
    WAITING_CANCEL_AFFIRM("waiting_cancel_affirm", "流标后等待第三方确认"),
    REPAYING("repaying", "还款中"),
    WAIT_REPAY_VERIFY("wait_repay_verify", "等待还款确认"),
    OVERDUE("overdue", "逾期"),
    COMPLETE("complete", "完成"),
    BAD_DEBT("bad_debt", "坏账");

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

    public static LoanStatus convertLoanStatus(com.tuotiansudai.repository.model.LoanStatus loanStatus) {
        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.PREHEAT) {
            return PREHEAT;
        }
        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.CANCEL) {
            return CANCEL;
        }

        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.COMPLETE) {
            return COMPLETE;
        }
        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.OVERDUE) {
            return OVERDUE;
        }
        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.RAISING) {
            return RAISING;
        }
        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.RECHECK) {
            return RECHECK;
        }
        if (loanStatus == com.tuotiansudai.repository.model.LoanStatus.REPAYING) {
            return REPAYING;
        }
        return RAISING;
    }
}
