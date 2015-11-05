package com.tuotiansudai.repository.model;

public enum  LoanStatus {
    PREHEAT("0","预热"),
    WAITING_VERIFY("","等待审核"),
    RECHECK("","等待复核"),
    CANCEL("4","流标"),
    RAISING("1","筹款中"),
    REPAYING("2","还款中"),
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
