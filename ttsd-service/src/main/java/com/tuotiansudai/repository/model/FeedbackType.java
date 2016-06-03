package com.tuotiansudai.repository.model;

public enum FeedbackType {
    opinion("意见"),
    complain("投诉"),
    consult("咨询"),
    other("其他");

    private String desc;

    FeedbackType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
