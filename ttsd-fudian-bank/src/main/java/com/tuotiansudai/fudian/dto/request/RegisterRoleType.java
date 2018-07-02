package com.tuotiansudai.fudian.dto.request;

public enum RegisterRoleType {

    INVESTER("3"),

    LOANER("1");

    private final String code;

    RegisterRoleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
