package com.tuotiansudai.repository.model;

public enum  LoanStatus {
    WAITING_VERIFY("等待审核"),
    VERIFY_FAIL("审核未通过"),
    RAISING("筹款中"),
    RECHECK("等待复核"),
    WAITING_RECHECK_VERIFY("放款后，等待确认"),
    CANCEL("流标"),
    REPAYING("还款中"),
    WAIT_REPAY_VERIFY("等待还款确认"),
    OVERDUE("逾期"),
    PREHEAT("预热"),
    COMPLETE("完成");

    private LoanStatus(String description){
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
