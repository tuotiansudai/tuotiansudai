package com.tuotiansudai.paywrapper.ghb.security.provider;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
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
            XmlMapper xmlMapper = new XmlMapper(new WstxInputFactory(), new WstxOutputFactory());
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return xmlMapper;
        }
    }
}
