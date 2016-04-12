package com.tuotiansudai.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseResponseDto<T extends BaseResponseDataDto> {
    public static final String SUCCESS_CODE = "0000";

    private String code;

    private String message;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS_CODE.equalsIgnoreCase(code);
    }


    public BaseResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponseDto(ReturnMessage returnMessage) {
        this.code = returnMessage.getCode();
        this.message = returnMessage.getMsg();
    }

    public BaseResponseDto() {

    }
}
