package com.tuotiansudai.fudian.dto.request;

public enum BankUserRole {

    INVESTOR("3"),

    LOANER("1");

    private final String code;

    BankUserRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
