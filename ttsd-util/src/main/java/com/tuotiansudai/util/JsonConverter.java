package com.tuotiansudai.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonConverter {

    public static  <T> T readValue(String content, Class<T> valueType){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String writeValueAsString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
