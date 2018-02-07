package com.tuotiansudai.repository.model;

public enum RepayStatus {

    REPAYING("待还", "待还款"),

    WAIT_PAY("等待支付", "待支付"),

    COMPLETE("完成", "已还款"),

    OVERDUE("逾期", "已逾期"),

    ALL("所有", "");

    private final String description;

    private final String viewText;

    public String getDescription() {
        return description;
    }

    public String getViewText() {
        return viewText;
    }

    RepayStatus(String description, String viewText) {
        this.description = description;
        this.viewText = viewText;
    }

}
