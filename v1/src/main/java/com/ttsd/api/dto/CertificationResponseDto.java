package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/7/28.
 */
public class CertificationResponseDto<T extends CertificationDataDto> {
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回码描述
     */
    private String message;

    /**
     * 实名认证返回的其它数据
     */
    private T data;

    /**
     * 返回码描述
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 返回码描述
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 返回码描述
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 返回码描述
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 实名认证返回的其它数据
     * @return
     */
    public T getData() {
        return data;
    }


    /**
     * 实名认证返回的其它数据
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }
}
