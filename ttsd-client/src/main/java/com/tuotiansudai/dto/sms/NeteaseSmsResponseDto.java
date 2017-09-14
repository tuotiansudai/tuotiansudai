package com.tuotiansudai.dto.sms;

import java.io.Serializable;

public class NeteaseSmsResponseDto implements Serializable {
    private String code;
    private String msg;
    private String obj;

    public boolean isSuccess() {
        return "200".equals(this.code);
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getObj() {
        return obj;
    }
}
