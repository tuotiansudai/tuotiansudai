package com.tuotiansudai.repository.model;

public enum RepayStatus {

    REPAYING("待还"),

    WAIT_PAY("等待支付"),

    COMPLETE("完成"),

    OVERDUE("逾期"),

    ALL("所有");

    private final String description;

    public String getDescription() {
        return description;
    }

    RepayStatus(String description) {
        this.description = description;
    }

}
