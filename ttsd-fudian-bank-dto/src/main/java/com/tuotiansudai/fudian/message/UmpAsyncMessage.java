package com.tuotiansudai.fudian.message;


import com.google.common.collect.Maps;

import java.util.Map;

public class UmpAsyncMessage extends BankBaseMessage{

    private String url;

    private Map<String, String> fields = Maps.newHashMap();

    public UmpAsyncMessage() {
    }

    public UmpAsyncMessage(boolean status, String url, Map<String, String> fields, String message) {
        super(status, message);
        this.url = url;
        this.fields = fields;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
