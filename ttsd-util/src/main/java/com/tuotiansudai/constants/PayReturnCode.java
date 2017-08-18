package com.tuotiansudai.constants;

public enum PayReturnCode {
    SUCCESS("0000"),

    ERROR("1001"),
    ERROR_TIMEOUT("1002");

    private final String value;

    PayReturnCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
