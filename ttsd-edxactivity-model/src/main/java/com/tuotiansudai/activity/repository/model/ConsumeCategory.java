package com.tuotiansudai.activity.repository.model;


public enum ConsumeCategory {
    ACCOUNT_POINT_BY_NO_POINT("积分", 0),
    ACCOUNT_POINT_BY_1000("积分", 1000),
    ACCOUNT_POINT_BY_10000("积分", 10000),
    TASK_COUNT("任务", 1);

    ConsumeCategory(String description, long point) {
        this.description = description;
        this.point = point;
    }

    String description;

    long point;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}
