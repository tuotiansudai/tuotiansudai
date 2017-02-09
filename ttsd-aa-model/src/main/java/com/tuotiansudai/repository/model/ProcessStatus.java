package com.tuotiansudai.repository.model;

public enum ProcessStatus {
    NOT_DONE("未处理"),
    DONE("已处理");

    private String desc;

    ProcessStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
