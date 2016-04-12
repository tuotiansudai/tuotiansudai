package com.tuotiansudai.repository.model;

public enum Environment {
    DEV, SMOKE, QA, STAGING, PRODUCTION;

    public static boolean isProduction(Environment environment){
        return environment.equals(Environment.PRODUCTION);
    }
}