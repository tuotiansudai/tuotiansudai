package com.tuotiansudai.api.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class CustomerObjectMapper extends ObjectMapper {

    public CustomerObjectMapper() {
        super();
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true); // 允许单引号
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        this.setDateFormat(dateFormat);
        GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        this.setTimeZone(gregorianCalendar.getTimeZone());
    }
}
