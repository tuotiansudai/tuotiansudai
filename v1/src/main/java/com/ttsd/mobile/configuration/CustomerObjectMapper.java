package com.ttsd.mobile.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerObjectMapper extends ObjectMapper {

    public CustomerObjectMapper() {
        super();
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); // 允许单引号
    }
}
