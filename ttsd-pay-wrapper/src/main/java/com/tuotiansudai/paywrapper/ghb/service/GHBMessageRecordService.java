package com.tuotiansudai.paywrapper.ghb.service;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.ghb.message.request.*;
import com.tuotiansudai.paywrapper.ghb.message.response.*;
import com.tuotiansudai.paywrapper.ghb.repository.mapper.DynamicTableMapper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GHBMessageRecordService {

    private static final int FIELD_MAX_LENGTH = 4096;

    private List<Class<?>> REQUEST_MESSAGE_CLASSES = Lists.newArrayList(RequestOGW00042.class, RequestOGW00043.class);
    private List<Class<?>> RESPONSE_MESSAGE_CLASSES = Lists.newArrayList(ResponseOGW00042.class, ResponseOGW00043.class);

    private final DynamicTableMapper dynamicTableMapper;

    @Autowired
    public GHBMessageRecordService(DynamicTableMapper dynamicTableMapper) {
        this.dynamicTableMapper = dynamicTableMapper;
    }

    public void autoCreateMessageRecordTables() {
        for (Class<?> cls : REQUEST_MESSAGE_CLASSES) {
            createRequestTableIfNotExist(cls);
        }
        for (Class<?> cls : RESPONSE_MESSAGE_CLASSES) {
            createResponseTableIfNotExist(cls);
        }
    }

    @Transactional(transactionManager = "ghbTransactionManager")
    public <T extends RequestBaseOGW> void saveRequestMessage(RequestMessageContent<T> data) {
        if (data == null) {
            return;
        }
        FieldValueCollection fieldValueCollection = new FieldValueCollection();
        fillRequestCommonFieldValues(fieldValueCollection, data);

        T xmlPara = data.getBody().getXmlpara();
        String tableName = xmlPara.getClass().getSimpleName();
        fillSpecialFieldValues(fieldValueCollection, xmlPara);

        fillCommonLiteralValue(fieldValueCollection, data.getPlainXMLPARA(), data.getCipherXMLPARA(), data.getFullMessage());

        dynamicTableMapper.insert(tableName, fieldValueCollection.getFields(), fieldValueCollection.getValues());
    }

    @Transactional(transactionManager = "ghbTransactionManager")
    public <T extends ResponseBaseOGW> void saveResponseMessage(ResponseMessageContent<T> data) {
        if (data == null) {
            return;
        }
        FieldValueCollection fieldValueCollection = new FieldValueCollection();
        fillResponseCommonFieldValues(fieldValueCollection, data);

        T content = data.getBody().getContent();
        String tableName = content.getClass().getSimpleName();
        fillSpecialFieldValues(fieldValueCollection, content);

        fillCommonLiteralValue(fieldValueCollection, data.getCipherXMLPARA(), data.getCipherXMLPARA(), data.getFullMessage());
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

        List<String> fieldDDLs = Lists.newArrayList();
        fillRequestCommonFieldDDLs(fieldDDLs);
        fillFieldDDLs(fieldDDLs, tClass);
        fillCommonFieldDDLs(fieldDDLs);

        if (AsyncRequestBaseOGW.class.isAssignableFrom(tClass)) {
            fieldDDLs.add("LATEST_RETURN_STATUS varchar(2)");
            fieldDDLs.add("LATEST_RETURN_STATUS_TIME datetime");
        }
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
        ddls.add(generateFieldDDL("channelCode", 12, true));
        ddls.add(generateFieldDDL("channelFlow", 56, true));
        ddls.add(generateFieldDDL("channelDate", 16, true));
        ddls.add(generateFieldDDL("channelTime", 12, true));
        ddls.add(generateFieldDDL("encryptData", 400, true));
        ddls.add(generateFieldDDL("TRANSCODE", 16, true));
        ddls.add(generateFieldDDL("MERCHANTID", 40, true));
        ddls.add(generateFieldDDL("APPID", 6, true));
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
        ddls.add(generateFieldDDL("channelCode", 12, true));
        ddls.add(generateFieldDDL("transCode", 16, true));
        ddls.add(generateFieldDDL("channelFlow", 40, true));
        ddls.add(generateFieldDDL("serverFlow", 40, true));
        ddls.add(generateFieldDDL("serverDate", 16, true));
        ddls.add(generateFieldDDL("serverTime", 12, true));
        ddls.add(generateFieldDDL("encryptData", 400, true));
        ddls.add(generateFieldDDL("status", 2, true));
        ddls.add(generateFieldDDL("errorCode", 24, true));
        ddls.add(generateFieldDDL("errorMsg", 600, true));
        ddls.add(generateFieldDDL("MERCHANTID", 64, true));
        ddls.add(generateFieldDDL("BANKID", 12, true));
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
        fieldValueCollection.addFieldValue("BANKID", body.getBankid());
    }

    // Common Message Table DDL

    private void fillCommonFieldDDLs(List<String> ddls) {
        ddls.add("PLAIN_XML_PARA text");
        ddls.add("CIPHER_XML_PARA text");
        ddls.add("FULL_MESSAGE text");
        ddls.add("created_time datetime not null");
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
        if (lengthProperty.max() > FIELD_MAX_LENGTH) {
            throw new AnnotationConfigurationException(String.format("[Length] annotation max value could not be more than %d on field [%s]", FIELD_MAX_LENGTH, field.getName()));
        }
        return generateFieldDDL(columnName, lengthProperty.max(), field.getAnnotation(NotBlank.class) != null);
    }

    private String generateFieldDDL(String columnName, int maxLength, boolean notNull) {
        return String.format("%s varchar(%d) %s", columnName, maxLength, notNull ? "not null" : "");
    }

    // Common Message Insert

    private void fillCommonLiteralValue(FieldValueCollection fieldValueCollection, String plainXmlpara, String cipherXmlpara, String fullMessage) {
        fieldValueCollection.addFieldValue("PLAIN_XML_PARA", plainXmlpara);
        fieldValueCollection.addFieldValue("CIPHER_XML_PARA", cipherXmlpara);
        fieldValueCollection.addFieldValue("FULL_MESSAGE", fullMessage);
        fieldValueCollection.addFieldValue("created_time", new Date());
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
        private List<String> fields = Lists.newArrayList();
        private List<Object> values = Lists.newArrayList();

        void addFieldValue(String field, Object value) {
            if (fields.contains(field)) {
                throw new DuplicateKeyException(String.format("field [%s] has already added", field));
            }
            fields.add(field);
            values.add(value);
        }

        List<String> getFields() {
            return fields;
        }

        public List<Object> getValues() {
            return values;
        }
    }
}
