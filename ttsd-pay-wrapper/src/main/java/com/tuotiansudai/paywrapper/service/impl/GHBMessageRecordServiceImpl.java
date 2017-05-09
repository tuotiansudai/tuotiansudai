package com.tuotiansudai.paywrapper.service.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tuotiansudai.paywrapper.ghb.message.request.*;
import com.tuotiansudai.paywrapper.ghb.message.response.*;
import com.tuotiansudai.paywrapper.repository.mapper.DynamicTableMapper;
import com.tuotiansudai.paywrapper.service.GHBMessageRecordService;
import com.tuotiansudai.repository.model.Source;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GHBMessageRecordServiceImpl implements GHBMessageRecordService {

    private int fieldMaxLength = 4096;
    private List<Class<?>> REQUEST_MESSAGE_CLASSES = Collections.singletonList(RequestOGW00042.class);
    private List<Class<?>> RESPONSE_MESSAGE_CLASSES = Collections.singletonList(ResponseOGW00042.class);

    @Autowired
    private DynamicTableMapper dynamicTableMapper;

    @Override
    public void autoCreateMessageRecordTables() {
        for (Class<?> cls : REQUEST_MESSAGE_CLASSES) {
            createRequestTableIfNotExist(cls);
        }
        for (Class<?> cls : RESPONSE_MESSAGE_CLASSES) {
            createResponseTableIfNotExist(cls);
        }

//        test();
    }

    /*
    private void test() {
        RequestOGW00042 requestOGW00042 = new RequestOGW00042(Source.IOS, "zhangzg", "11011011011", "13800138000");
        RequestMessageContent<RequestOGW00042> requestData = new RequestMessageContent<>(requestOGW00042);
        saveRequestMessage(requestData, "xxx", "xxxxx");

        ResponseOGW00042 responseOGW00042 = new ResponseOGW00042();
        responseOGW00042.setAcname("what");
        responseOGW00042.setAcno("123");
        responseOGW00042.setIdno("1101231234");
        responseOGW00042.setIdtype("helloidtype");
        responseOGW00042.setMobile("13800138000");
        responseOGW00042.setOldreqseqno("1234335");

        ResponseMessageContent<ResponseOGW00042> responseData = new ResponseMessageContent<>();
        ResponseMessageHeader responseMessageHeader = new ResponseMessageHeader();
        responseData.setHeader(responseMessageHeader);

        ResponseOGW00042 content = new ResponseOGW00042();
        content.setOldreqseqno("1234");
        content.setIdno("hello");

        ResponseMessageBody<ResponseOGW00042> responseBody = new ResponseMessageBody<>();
        responseBody.setBankid("bankId");
        responseBody.setMerchantid("merId");
        responseBody.setTranscode("trancode");
        responseBody.setContent(content);
        responseData.setBody(responseBody);

        saveResponseMessage(responseData, "yyy", "yyyyyyy");
    }
    */

    @Override
    @Transactional(transactionManager = "payTransactionManager")
    public <T extends RequestBaseOGW> void saveRequestMessage(RequestMessageContent<T> data) {
        if (data == null) {
            return;
        }
        FieldValueCollection fieldValueCollection = new FieldValueCollection();
        fillRequestCommonFieldValues(fieldValueCollection, data);

        T xmlPara = data.getBody().getXmlpara();
        String tableName = xmlPara.getClass().getSimpleName();
        fillSpecialFieldValues(fieldValueCollection, xmlPara);

        fillCommonLiteralValue(fieldValueCollection, data.getPlainXMLPARA(), data.getFullMessage());

        dynamicTableMapper.insert(tableName, fieldValueCollection.getFields(), fieldValueCollection.getValues());
    }

    @Override
    @Transactional(transactionManager = "payTransactionManager")
    public <T extends ResponseBaseOGW> void saveResponseMessage(ResponseMessageContent<T> data, String originXmlpara, String fullMessage) {
        if (data == null) {
            return;
        }
        FieldValueCollection fieldValueCollection = new FieldValueCollection();
        fillResponseCommonFieldValues(fieldValueCollection, data);

        T content = data.getBody().getContent();
        String tableName = content.getClass().getSimpleName();
        fillSpecialFieldValues(fieldValueCollection, content);

        fillCommonLiteralValue(fieldValueCollection, originXmlpara, fullMessage);
        dynamicTableMapper.insert(tableName, fieldValueCollection.getFields(), fieldValueCollection.getValues());
    }

    private boolean isTableExists(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return true;
        }
        String tableNameInDb = dynamicTableMapper.findTable(tableName);
        return tableName.equalsIgnoreCase(tableNameInDb);
    }

    private void createRequestTableIfNotExist(Class<?> tClass) {
        String tableName = tClass.getSimpleName();
        if (isTableExists(tableName)) {
            return;
        }

        List<String> fieldDDLs = new ArrayList<>();
        fillRequestCommonFieldDDLs(fieldDDLs);
        fillFieldDDLs(fieldDDLs, tClass);
        fillCommonFieldDDLs(fieldDDLs);

        dynamicTableMapper.createTable(tableName, fieldDDLs, Collections.emptyList());
    }

    private void createResponseTableIfNotExist(Class<?> tClass) {
        String tableName = tClass.getSimpleName();
        if (isTableExists(tableName)) {
            return;
        }

        List<String> fieldDDLs = new ArrayList<>();
        fillResponseCommonFieldDDLs(fieldDDLs);
        fillFieldDDLs(fieldDDLs, tClass);
        fillCommonFieldDDLs(fieldDDLs);

        dynamicTableMapper.createTable(tableName, fieldDDLs, Collections.emptyList());
    }


    // Request Message Table DDL

    private void fillRequestCommonFieldDDLs(List<String> ddls) {
        ddls.add(generateFieldDDL("channelCode", 6));
        ddls.add(generateFieldDDL("channelFlow", 28));
        ddls.add(generateFieldDDL("channelDate", 8));
        ddls.add(generateFieldDDL("channelTime", 6));
        ddls.add(generateFieldDDL("encryptData", 200));
        ddls.add(generateFieldDDL("TRANSCODE", 8));
        ddls.add(generateFieldDDL("MERCHANTID", 20));
        ddls.add(generateFieldDDL("APPID", 3));
    }

    // Request Message Insert

    private <T extends RequestBaseOGW> void fillRequestCommonFieldValues(FieldValueCollection fieldValueCollection, RequestMessageContent<T> data) {
        RequestMessageHeader header = data.getHeader();
        RequestMessageBody<T> body = data.getBody();
        T xmlPara = body.getXmlpara();
        fieldValueCollection.addFieldValue("channelCode", header.getChannelCode());
        fieldValueCollection.addFieldValue("channelFlow", header.getChannelFlow());
        fieldValueCollection.addFieldValue("channelDate", header.getChannelDate());
        fieldValueCollection.addFieldValue("channelTime", header.getChannelTime());
        fieldValueCollection.addFieldValue("encryptData", header.getEncryptData());
        fieldValueCollection.addFieldValue("TRANSCODE", body.getTranscode());
        fieldValueCollection.addFieldValue("MERCHANTID", xmlPara.getMerchantid());
        fieldValueCollection.addFieldValue("APPID", xmlPara.getAppid());
    }

    // Response Message Table DDL

    private void fillResponseCommonFieldDDLs(List<String> ddls) {
        ddls.add(generateFieldDDL("channelCode", 6));
        ddls.add(generateFieldDDL("transCode", 8));
        ddls.add(generateFieldDDL("channelFlow", 20));
        ddls.add(generateFieldDDL("serverFlow", 20));
        ddls.add(generateFieldDDL("serverDate", 8));
        ddls.add(generateFieldDDL("serverTime", 6));
        ddls.add(generateFieldDDL("encryptData", 200));
        ddls.add(generateFieldDDL("status", 1));
        ddls.add(generateFieldDDL("errorCode", 12));
        ddls.add(generateFieldDDL("errorMsg", 300));
        ddls.add(generateFieldDDL("MERCHANTID", 32));
        ddls.add(generateFieldDDL("X_TRANSCODE", 8));
        ddls.add(generateFieldDDL("BANKID", 6));
    }

    // Response Message Insert

    private <T extends ResponseBaseOGW> void fillResponseCommonFieldValues(FieldValueCollection fieldValueCollection, ResponseMessageContent<T> data) {
        ResponseMessageHeader header = data.getHeader();
        ResponseMessageBody<T> body = data.getBody();
        fieldValueCollection.addFieldValue("channelCode", header.getChannelCode());
        fieldValueCollection.addFieldValue("transCode", header.getTransCode());
        fieldValueCollection.addFieldValue("channelFlow", header.getChannelFlow());
        fieldValueCollection.addFieldValue("serverFlow", header.getServerFlow());
        fieldValueCollection.addFieldValue("serverDate", header.getServerDate());
        fieldValueCollection.addFieldValue("serverTime", header.getServerTime());
        fieldValueCollection.addFieldValue("encryptData", header.getEncryptData());
        fieldValueCollection.addFieldValue("status", header.getStatus());
        fieldValueCollection.addFieldValue("errorCode", header.getErrorCode());
        fieldValueCollection.addFieldValue("errorMsg", header.getErrorMsg());
        fieldValueCollection.addFieldValue("MERCHANTID", body.getMerchantid());
        fieldValueCollection.addFieldValue("X_TRANSCODE", body.getTranscode());
        fieldValueCollection.addFieldValue("BANKID", body.getBankid());
    }

    // Common Message Table DDL

    private void fillCommonFieldDDLs(List<String> ddls) {
        ddls.add("XML_PARA text");
        ddls.add("FULL_MESSAGE text");
        ddls.add("crTime BIGINT UNSIGNED");
    }

    private void fillFieldDDLs(List<String> ddls, Class<?> tClass) {
        try {
            for (Field f : tClass.getDeclaredFields()) {
                JacksonXmlProperty xmlProperty = f.getAnnotation(JacksonXmlProperty.class);
                if (xmlProperty != null) {
                    if (StringUtils.isEmpty(xmlProperty.localName())) {
                        throw new AnnotationConfigurationException(String.format("[Length] annotation localName could not be empty on field [%s]", f.getName()));
                    }
                    ddls.add(generateFieldDDL(xmlProperty.localName(), f));
                }
            }
        } catch (AnnotationConfigurationException e) {
            throw new AnnotationConfigurationException(String.format("generate table ddl failed, class: %s, error:%s", tClass.getName(), e.getMessage()));
        }
    }

    private String generateFieldDDL(String columnName, Field field) {
        Length lengthProperty = field.getAnnotation(Length.class);
        if (lengthProperty == null) {
            throw new AnnotationConfigurationException(String.format("[Length] annotation missed on field [%s]", field.getName()));
        }
        if (lengthProperty.max() > fieldMaxLength) {
            throw new AnnotationConfigurationException(String.format("[Length] annotation max value could not be more than %d on field [%s]", fieldMaxLength, field.getName()));
        }
        return generateFieldDDL(columnName, lengthProperty.max());
    }

    private String generateFieldDDL(String columnName, int maxLength) {
        return String.format("%s varchar(%d)", columnName, maxLength);
    }

    // Common Message Insert

    private void fillCommonLiteralValue(FieldValueCollection fieldValueCollection, String originXmlpara, String fullMessage) {
        fieldValueCollection.addFieldValue("XML_PARA", originXmlpara);
        fieldValueCollection.addFieldValue("FULL_MESSAGE", fullMessage);
        fieldValueCollection.addFieldValue("crTime", System.currentTimeMillis());
    }

    private void fillSpecialFieldValues(FieldValueCollection fieldValueCollection, Object xmlPara) {
        try {
            Class<?> tClass = xmlPara.getClass();
            for (Field field : tClass.getDeclaredFields()) {
                JacksonXmlProperty xmlProperty = field.getAnnotation(JacksonXmlProperty.class);
                if (xmlProperty != null && StringUtils.isNotEmpty(xmlProperty.localName())) {
                    field.setAccessible(true);
                    fieldValueCollection.addFieldValue(xmlProperty.localName(), String.valueOf(field.get(xmlPara)));
                }
            }
        } catch (IllegalAccessException | DuplicateKeyException e) {
            throw new RuntimeException(String.format("fill field and value failed for class[%s]", xmlPara.getClass().getName()), e);
        }
    }

    // Helper classes

    class FieldValueCollection {
        private List<String> fields = new ArrayList<>();
        private List<Object> values = new ArrayList<>();

        public void addFieldValue(String field, Object value) {
            if (fields.contains(field)) {
                throw new DuplicateKeyException(String.format("field [%s] has already added", field));
            }
            fields.add(field);
            values.add(value);
        }

        public List<String> getFields() {
            return fields;
        }

        public List<Object> getValues() {
            return values;
        }
    }
}
