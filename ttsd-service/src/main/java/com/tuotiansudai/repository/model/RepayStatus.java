package com.tuotiansudai.repository.model;

/**
 * Created by Administrator on 2015/9/6.
 */
public enum RepayStatus {

    COMPLETE("等待确认"),
    REPAYING("投资成功"),
    OVERDUE("投资失败");

    private final String description;

    public String getDescription() {
        return description;
    }

    RepayStatus(String description) {
        this.description = description;
    }

}
