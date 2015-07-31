package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/7/29.
 */
public class RetrievePasswordResponseDto <T extends DataDto>{
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回码描述
     */
    private String message;

    /**
     * 待封装的其他数据
     */
    private T data;

    /**
     * 返回码
     */
    public String getCode() {
        return code;
    }

    /**
     * 返回码
     */
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
}
