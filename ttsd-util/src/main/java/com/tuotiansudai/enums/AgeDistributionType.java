package com.tuotiansudai.enums;

public enum AgeDistributionType {
    UNDER_20("20岁以下"),
    BETWEEN_20_AND_30("20~30岁"),
    BETWEEN_30_AND_40("30~40岁"),
    BETWEEN_40_AND_50("40~50岁"),
    MORE_THAN_50("50岁以上");

    private final String description;

    AgeDistributionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
