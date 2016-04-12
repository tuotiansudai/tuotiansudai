package com.tuotiansudai.repository.model;

public enum LoanPeriodUnit {
    DAY("日"),
    MONTH("月");

    private String desc;

    private LoanPeriodUnit(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
