package com.tuotiansudai.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonConverter{

    public static  <T> T readValue(String content, Class<T> valueType) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(content, valueType);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
    }
}
