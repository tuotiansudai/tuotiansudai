package com.tuotiansudai.activity.repository.model;

public enum NationalMidAutumnLoanType {

    InvestCash("逢万返百"),
    InvestCoupon("加息6.8%");

    String desc;

    NationalMidAutumnLoanType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
