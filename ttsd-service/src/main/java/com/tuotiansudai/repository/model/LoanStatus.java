package com.tuotiansudai.repository.model;

public enum  LoanStatus {
    WAITING_VERIFY("","等待审核"),
    VERIFY_FAIL("","审核未通过"),
    PREHEAT("0","预热"),
    RAISING("1","筹款中"),
    REPAYING("2","还款中"),
    RECHECK("","等待复核"),
    WAITING_RECHECK_VERIFY("","放款后，等待确认"),
    CANCEL("4","流标"),
    WAIT_REPAY_VERIFY("","等待还款确认"),
    OVERDUE("","逾期"),
    COMPLETE("4","完成");

    LoanStatus(String code,String description){
        this.description = description;
        this.code = code;
    }

    private String code;

    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
