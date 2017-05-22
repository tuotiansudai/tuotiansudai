package com.tuotiansudai.paywrapper.ghb.service;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GHBMessageRecordService {

    private static final int FIELD_MAX_LENGTH = 4096;

    //    private List<Class<?>> REQUEST_MESSAGE_CLASSES = Lists.newArrayList(RequestOGW00042.class, RequestOGW00043.class, RequestOGW00051.class, RequestOGW00051BWLISTItem.class);
    private List<Class<?>> REQUEST_MESSAGE_CLASSES = Lists.newArrayList(RequestOGW00051.class, RequestOGW00051BWLISTItem.class);
    //    private List<Class<?>> RESPONSE_MESSAGE_CLASSES = Lists.newArrayList(ResponseOGW00042.class, ResponseOGW00043.class, ResponseOGW00066.class, ResponseOGW00066RSLISTItem.class);
    private List<Class<?>> RESPONSE_MESSAGE_CLASSES = Lists.newArrayList(ResponseOGW00066.class, ResponseOGW00066RSLISTItem.class);

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

    @Transactional(transactionManager = "ghbTransactionManager", rollbackFor = Exception.class)
    public <T extends RequestBaseOGW> void saveRequestMessage(RequestMessageContent<T> data) throws IllegalAccessException {
        if (data == null) {
            return;
        }
        FieldValueCollection fieldValueCollection = new FieldValueCollection();
        fillRequestCommonFieldValues(fieldValueCollection, data);

        T xmlPara = data.getBody().getXmlpara();
        String tableName = xmlPara.getClass().getSimpleName();
        fillSpecialFieldValues(fieldValueCollection, xmlPara);

        fillCommonLiteralValue(fieldValueCollection, data.getPlainXMLPARA(), data.getCipherXMLPARA(), data.getFullMessage());

        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", null);
        map.put("tableName", tableName);
        map.put("columns", fieldValueCollection.getFields());
        map.put("values", fieldValueCollection.getValues());

        dynamicTableMapper.insert(map);

        saveListItem(xmlPara, (Long) map.get("id"));
    }

    @Transactional(transactionManager = "ghbTransactionManager", rollbackFor = Exception.class)
    public <T extends ResponseBaseOGW> void saveResponseMessage(ResponseMessageContent<T> data) throws IllegalAccessException {
        if (data == null) {
            return;
        }
        FieldValueCollection fieldValueCollection = new FieldValueCollection();
        fillResponseCommonFieldValues(fieldValueCollection, data);

        T content = data.getBody().getContent();
        String tableName = content.getClass().getSimpleName();
        fillSpecialFieldValues(fieldValueCollection, content);

        fillCommonLiteralValue(fieldValueCollection, data.getCipherXMLPARA(), data.getCipherXMLPARA(), data.getFullMessage());

        HashMap<String, Object> map = Maps.newHashMap();
        map.put("id", null);
        map.put("tableName", tableName);
        map.put("columns", fieldValueCollection.getFields());
        map.put("values", fieldValueCollection.getValues());

        dynamicTableMapper.insert(map);

        saveListItem(content, (Long) map.get("id"));
    }

    private <T> void saveListItem(T xmlPara, long parentId) throws IllegalAccessException {
        FieldValueCollection fieldValueCollection;

        List<Field> fields = Lists.newArrayList(xmlPara.getClass().getDeclaredFields())
                .stream()
                .filter(field -> field.getAnnotation(JacksonXmlProperty.class) != null && List.class == field.getType())
                .collect(Collectors.toList());

        for (Field field : fields) {
            field.setAccessible(true);
            List list = (List) field.get(xmlPara);
            for (Object item : list) {
                fieldValueCollection = new FieldValueCollection();
                fieldValueCollection.addFieldValue("parent_id", parentId);
                fillSpecialFieldValues(fieldValueCollection, item);

                dynamicTableMapper.insert(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                        .put("tableName", item.getClass().getSimpleName())
                        .put("columns", fieldValueCollection.getFields())
                        .put("values", fieldValueCollection.getValues())
                        .build()));
            }
        }
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

        String foreignTableName = "";
        try {
            Field parentRequest = tClass.getField("PARENT_REQUEST");
            Class aClass = (Class) parentRequest.get(tClass);
            foreignTableName = aClass.getSimpleName();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }

        if (Strings.isNullOrEmpty(foreignTableName)) {
            fillRequestCommonFieldDDLs(fieldDDLs);
        }

        fillFieldDDLs(fieldDDLs, tClass);

        if (Strings.isNullOrEmpty(foreignTableName)) {
            fillCommonFieldDDLs(fieldDDLs);
        }

        if (AsyncRequestBaseOGW.class.isAssignableFrom(tClass)) {
            fieldDDLs.add("LATEST_RETURN_STATUS varchar(2)");
            fieldDDLs.add("LATEST_RETURN_STATUS_TIME datetime");
        }

        dynamicTableMapper.createTable(tableName, foreignTableName, fieldDDLs, Collections.emptyList(), !Strings.isNullOrEmpty(foreignTableName));
    }

    private void createResponseTableIfNotExist(Class<?> tClass) {
        String tableName = tClass.getSimpleName();
        if (isTableExists(tableName)) {
            return;
        }

        List<String> fieldDDLs = Lists.newArrayList();

        String foreignTableName = "";
        try {
            Field parentRequest = tClass.getField("PARENT_RESPONSE");
            Class aClass = (Class) parentRequest.get(tClass);
            foreignTableName = aClass.getSimpleName();
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }

        if (Strings.isNullOrEmpty(foreignTableName)) {
            fillResponseCommonFieldDDLs(fieldDDLs);
        }

        fillFieldDDLs(fieldDDLs, tClass);

        if (Strings.isNullOrEmpty(foreignTableName)) {
            fillCommonFieldDDLs(fieldDDLs);
        }

        dynamicTableMapper.createTable(tableName, foreignTableName, fieldDDLs, Collections.emptyList(), !Strings.isNullOrEmpty(foreignTableName));
    }

    // Request Message Table DDL


    private void fillRequestCommonFieldDDLs(List<String> ddls) {
        ddls.add(generateFieldDDL("header_channelCode", 12, true));
        ddls.add(generateFieldDDL("header_channelFlow", 56, true));
        ddls.add(generateFieldDDL("header_channelDate", 16, true));
        ddls.add(generateFieldDDL("header_channelTime", 12, true));
        ddls.add(generateFieldDDL("header_encryptData", 400, false));
        ddls.add(generateFieldDDL("BODY_TRANSCODE", 16, true));
        ddls.add(generateFieldDDL("BODY_MERCHANTID", 40, true));
        ddls.add(generateFieldDDL("BODY_APPID", 6, true));
    }

    // Request Message Insert

    private <T extends RequestBaseOGW> void fillRequestCommonFieldValues(FieldValueCollection fieldValueCollection, RequestMessageContent<T> data) {
        RequestMessageHeader header = data.getHeader();
        RequestMessageBody<T> body = data.getBody();
        T xmlPara = body.getXmlpara();
        fieldValueCollection.addFieldValue("header_channelCode", header.getChannelCode());
        fieldValueCollection.addFieldValue("header_channelFlow", header.getChannelFlow());
        fieldValueCollection.addFieldValue("header_channelDate", header.getChannelDate());
        fieldValueCollection.addFieldValue("header_channelTime", header.getChannelTime());
        fieldValueCollection.addFieldValue("header_encryptData", header.getEncryptData());
        fieldValueCollection.addFieldValue("BODY_TRANSCODE", body.getTranscode());
        fieldValueCollection.addFieldValue("BODY_MERCHANTID", xmlPara.getMerchantid());
        fieldValueCollection.addFieldValue("BODY_APPID", xmlPara.getAppid());
    }

    // Response Message Table DDL

    private void fillResponseCommonFieldDDLs(List<String> ddls) {
        ddls.add(generateFieldDDL("header_channelCode", 12, true));
        ddls.add(generateFieldDDL("header_transCode", 16, true));
        ddls.add(generateFieldDDL("header_channelFlow", 40, true));
        ddls.add(generateFieldDDL("header_serverFlow", 40, true));
        ddls.add(generateFieldDDL("header_serverDate", 16, true));
        ddls.add(generateFieldDDL("header_serverTime", 12, true));
        ddls.add(generateFieldDDL("header_encryptData", 400, false));
        ddls.add(generateFieldDDL("header_status", 2, true));
        ddls.add(generateFieldDDL("header_errorCode", 24, true));
        ddls.add(generateFieldDDL("header_errorMsg", 600, false));
        ddls.add(generateFieldDDL("BODY_MERCHANTID", 64, true));
        ddls.add(generateFieldDDL("BODY_BANKID", 12, true));
    }

    // Response Message Insert

    private <T extends ResponseBaseOGW> void fillResponseCommonFieldValues(FieldValueCollection fieldValueCollection, ResponseMessageContent<T> data) {
        ResponseMessageHeader header = data.getHeader();
        ResponseMessageBody<T> body = data.getBody();
        fieldValueCollection.addFieldValue("header_channelCode", header.getChannelCode());
        fieldValueCollection.addFieldValue("header_transCode", header.getTransCode());
        fieldValueCollection.addFieldValue("header_channelFlow", header.getChannelFlow());
        fieldValueCollection.addFieldValue("header_serverFlow", header.getServerFlow());
        fieldValueCollection.addFieldValue("header_serverDate", header.getServerDate());
        fieldValueCollection.addFieldValue("header_serverTime", header.getServerTime());
        fieldValueCollection.addFieldValue("header_encryptData", header.getEncryptData());
        fieldValueCollection.addFieldValue("header_status", header.getStatus());
        fieldValueCollection.addFieldValue("header_errorCode", header.getErrorCode());
        fieldValueCollection.addFieldValue("header_errorMsg", header.getErrorMsg());
        fieldValueCollection.addFieldValue("BODY_MERCHANTID", body.getMerchantid());
        fieldValueCollection.addFieldValue("BODY_BANKID", body.getBankid());
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
                    if (List.class == f.getType()) {
                        continue;
                    }
                    if (Strings.isNullOrEmpty(xmlProperty.localName())) {
                        throw new AnnotationConfigurationException(String.format("annotation localName could not be empty on field [%s]", f.getName()));
                    }
                    JacksonXmlRootElement annotation = tClass.getAnnotation(JacksonXmlRootElement.class);
                    ddls.add(generateFieldDDL(annotation != null ? annotation.localName() + "_" : "BODY_"
                            , xmlProperty.localName(), f));
                }
            }
        } catch (AnnotationConfigurationException e) {
            throw new AnnotationConfigurationException(String.format("generate table ddl failed, class: %s, error:%s", tClass.getName(), e.getMessage()));
        }
    }

    private String generateFieldDDL(String prefix, String columnName, Field field) {
        Length lengthProperty = field.getAnnotation(Length.class);
        if (lengthProperty == null) {
            throw new AnnotationConfigurationException(String.format("[Length] annotation missed on field [%s]", field.getName()));
        }
        if (lengthProperty.max() > FIELD_MAX_LENGTH) {
            throw new AnnotationConfigurationException(String.format("[Length] annotation max value could not be more than %d on field [%s]", FIELD_MAX_LENGTH, field.getName()));
        }
        return generateFieldDDL(prefix + columnName, lengthProperty.max(), field.getAnnotation(NotBlank.class) != null);
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

    private void fillSpecialFieldValues(FieldValueCollection fieldValueCollection, Object xmlPara) throws IllegalAccessException {
        Class<?> tClass = xmlPara.getClass();
        for (Field field : tClass.getDeclaredFields()) {
            JacksonXmlProperty xmlProperty = field.getAnnotation(JacksonXmlProperty.class);
            if (xmlProperty != null && List.class != field.getType() && StringUtils.isNotEmpty(xmlProperty.localName())) {
                field.setAccessible(true);
                JacksonXmlRootElement annotation = tClass.getAnnotation(JacksonXmlRootElement.class);
                fieldValueCollection.addFieldValue(
                        annotation != null ? MessageFormat.format("{0}_{1}", annotation.localName(), xmlProperty.localName()) : "BODY_" + xmlProperty.localName(),
                        String.valueOf(field.get(xmlPara)));
            }
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
