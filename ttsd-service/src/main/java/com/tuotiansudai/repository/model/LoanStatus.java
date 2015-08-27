package com.tuotiansudai.repository.model;

public enum  LoanStatus {
    WAITING_VERIFY("waiting_verify","等待审核"),
    VERIFY_FAIL("verify_fail","审核未通过"),
    RAISING("raising","筹款中"),
    RECHECK("recheck","等待复核"),
    WAITING_RECHECK_VERIFY("waiting_recheck_verify","放款后，等待确认"),
    CANCEL("cancel","流标"),
    REPAYING("repaying","还款中"),
    WAIT_REPAY_VERIFY("wait_repay_verify","等待还款确认"),
    OVERDUE("overdue","逾期"),
    COMPLETE("complete","完成");

    private LoanStatus(String code,String description){
        this.code = code;
        this.description = description;
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
