package com.tuotiansudai.repository.model;

public enum LoanApplicationRegion {
    BEI_JING("北京"),
    SHI_JIA_ZHUANG("石家庄"),
    CHENG_DE("承德"),
    JI_NING("济宁");

    private String description;

    public String getDescription() {
        return description;
    }

    LoanApplicationRegion(String description) {
        this.description = description;
    }
}
