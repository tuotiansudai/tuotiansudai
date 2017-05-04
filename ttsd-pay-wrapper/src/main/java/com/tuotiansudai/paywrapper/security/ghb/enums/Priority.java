package com.tuotiansudai.paywrapper.security.ghb.enums;

public enum Priority {

    NORMAL(1), URGENT(2), VERY_URGENT(3);

    private final int code;

    Priority(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
