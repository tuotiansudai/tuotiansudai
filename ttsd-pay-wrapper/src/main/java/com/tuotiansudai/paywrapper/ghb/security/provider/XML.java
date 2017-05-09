package com.tuotiansudai.paywrapper.ghb.security.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class XML {

    private static final Log logger = LogFactory.getLog(XML.class);

    private static ThreadLocalXmlMapper xmlMapper = new ThreadLocalXmlMapper();

    public static String serializer(Object data) {
        try {
            return xmlMapper.get().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.warn("serializer xml failed", e);
        }
        return null;
    }

    public static <T> T deserializer(String encrypted, Class<T> tClass) {
        try {
            return xmlMapper.get().readValue(encrypted, tClass);
        } catch (IOException e) {
            logger.warn("deserializer xml failed", e);
        }
        return null;
    }

    public static <T> T deserializer(String xml, TypeReference<T> typeReference){
        try {
            return xmlMapper.get().readValue(xml, typeReference);
        } catch (IOException e) {
            logger.warn("deserializer xml failed", e);
        }
        return null;
    }

    private static class ThreadLocalXmlMapper extends ThreadLocal<XmlMapper> {
        @Override
        protected XmlMapper initialValue() {
            return new XmlMapper();
        }
    }
}
