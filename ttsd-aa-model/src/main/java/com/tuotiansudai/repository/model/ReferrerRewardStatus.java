package com.tuotiansudai.repository.model;

public enum ReferrerRewardStatus {

    SUCCESS("发放成功"),

    FAILURE("发放失败"),

    NO_ACCOUNT("推荐人未实名"),

    FORBIDDEN("线下发放");

    private final String description;

    ReferrerRewardStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
