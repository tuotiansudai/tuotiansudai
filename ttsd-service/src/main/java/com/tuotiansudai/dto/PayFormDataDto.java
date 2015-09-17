package com.tuotiansudai.dto;

import com.google.common.collect.Maps;

import java.util.Map;

public class PayFormDataDto extends BaseDataDto {

    private String url;

    private Map<String, String> fields = Maps.newHashMap();

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
