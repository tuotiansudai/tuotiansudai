package com.tuotiansudai.fudian.dto;

import java.io.Serializable;

public class BankAsyncData implements Serializable {

    private String url;

    private String data;

    private boolean status;

    private String message;

    public BankAsyncData() {
    }

    public BankAsyncData(String url, String data, boolean status, String message) {
        this.url = url;
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
