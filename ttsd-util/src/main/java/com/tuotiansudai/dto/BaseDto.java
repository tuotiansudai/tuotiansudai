package com.tuotiansudai.dto;

public class BaseDto<T extends BaseDataDto> {

    private boolean success = true;

    private T data;

    public BaseDto() {
    }

    public BaseDto(boolean success) {
        this.success = success;
    }

    public BaseDto(T data) {
        this.data = data;
    }

    public BaseDto(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
