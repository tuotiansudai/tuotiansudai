package com.tuotiansudai.dto;

public class BaseWrapperDataDto<T> extends BaseDataDto {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
