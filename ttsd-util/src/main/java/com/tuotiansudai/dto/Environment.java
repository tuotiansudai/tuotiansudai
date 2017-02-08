package com.tuotiansudai.dto;

public enum Environment {
    DEV,
    SMOKE,
    QA,
    PRODUCTION;

    public static boolean isProduction(Environment environment){
        return Environment.PRODUCTION == environment;
    }
}