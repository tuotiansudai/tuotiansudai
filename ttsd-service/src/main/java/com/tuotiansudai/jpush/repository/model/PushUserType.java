package com.tuotiansudai.jpush.repository.model;

public enum PushUserType {
    ALL("ALL"),
    STAFF("业务员"),
    AGENT("渠道用户"),
    RECOMMENDATION("业务员的一级推荐"),
    OTHERS("自然用户");

    private String description;

    PushUserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
