package com.tuotiansudai.enums;

public enum AgeNewDistributionType {
    UNDER_20("20岁以下", 1),
    BETWEEN_20_AND_35("20~35岁", 2),
    BETWEEN_35_AND_50("35~50岁", 3),
    MORE_THAN_50("50岁以上", 4);

    private String description;

    private int ageStage;

    AgeNewDistributionType(String description, int ageStage) {
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
        for (AgeNewDistributionType ageDistributionType : AgeNewDistributionType.values()) {
            if (ageDistributionType.ageStage == ageStage) {
                return ageDistributionType.getDescription();
            }
        }
        return null;
    }
}
