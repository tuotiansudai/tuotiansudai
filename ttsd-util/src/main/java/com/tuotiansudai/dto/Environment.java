package com.tuotiansudai.dto;

public enum Environment {
    DEV,
    SMOKE,
    QA1,
    QA2,
    QA3,
    QA4,
    QA5,
    UT,
    PRODUCTION;

    public static boolean isProduction(Environment environment){
        return Environment.PRODUCTION == environment;
    }
}