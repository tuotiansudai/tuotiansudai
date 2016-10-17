package com.tuotiansudai.repository.model;

public enum FeedbackProcessStatus {
    NOT_DONE("未处理"),
    DONE("已处理");

    private String desc;

    FeedbackProcessStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
