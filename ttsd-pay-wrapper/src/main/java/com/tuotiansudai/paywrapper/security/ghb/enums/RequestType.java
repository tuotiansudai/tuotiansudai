package com.tuotiansudai.paywrapper.security.ghb.enums;

public enum RequestType {
    SYNC(1), ASYNC(2);

    private final int code;

    RequestType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
