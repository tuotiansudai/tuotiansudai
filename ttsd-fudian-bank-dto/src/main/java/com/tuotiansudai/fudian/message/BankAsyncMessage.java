package com.tuotiansudai.fudian.message;

public class BankAsyncMessage extends BankBaseMessage {

    private String url;

    private String data;

    public BankAsyncMessage() {
    }

    public BankAsyncMessage(String url, String data, boolean status, String message) {
        super(status, message);
        this.url = url;
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }
}
