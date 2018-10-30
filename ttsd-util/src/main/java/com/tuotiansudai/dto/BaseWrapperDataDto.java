package com.tuotiansudai.dto;

public class BaseWrapperDataDto<T> extends BaseDataDto {
    public BaseWrapperDataDto() {
    }

    public BaseWrapperDataDto(boolean status, String msg, T data) {
        super(status, msg);
        this.data = data;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
