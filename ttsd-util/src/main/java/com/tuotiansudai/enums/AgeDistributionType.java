package com.tuotiansudai.enums;

public enum AgeDistributionType {
    UNDER_20("20岁以下", 1),
    BETWEEN_20_AND_30("20~30岁", 2),
    BETWEEN_30_AND_40("30~40岁", 3),
    BETWEEN_40_AND_50("40~50岁", 4),
    MORE_THAN_50("50岁以上", 5);

    private String description;

    private int ageStage;

    AgeDistributionType(String description, int ageStage) {
        this.description = description;
        this.ageStage = ageStage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgeStage() {
        return ageStage;
    }

    public void setAgeStage(int ageStage) {
        this.ageStage = ageStage;
    }

    public static String getNameByAgeStage(int ageStage) {
        for (AgeDistributionType ageDistributionType : AgeDistributionType.values()) {
            if (ageDistributionType.ageStage == ageStage) {
                return ageDistributionType.getDescription();
            }
        }
        return null;
    }
}
