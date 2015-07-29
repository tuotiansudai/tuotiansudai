package com.ttsd.api.dto;

/**
 * Created by hourglasskoala on 15/7/29.
 */
public class LogInResponseDto<T extends LogInDataDto> {
    private String code;
    private String message;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
