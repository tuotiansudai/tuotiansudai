package com.tuotiansudai.activity.repository.dto;


public class BaseResponse<T> {

    private boolean status;

    private String message;

    private T data;

    public BaseResponse() {
    }

    public BaseResponse(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
