package com.tuotiansudai.api.dto.v1_0;

public class DomobNotifyResponseDto {
    private final String message;
    private final boolean success;

    public DomobNotifyResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
