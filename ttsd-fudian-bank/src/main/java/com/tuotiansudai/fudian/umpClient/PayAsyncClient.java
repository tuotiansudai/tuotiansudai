package com.tuotiansudai.fudian.umpClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.dto.umpRequest.BaseAsyncRequestModel;
import com.tuotiansudai.fudian.dto.umpResponse.BaseCallbackRequestModel;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Component
public class PayAsyncClient implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    static Logger logger = Logger.getLogger(PayAsyncClient.class);

    private ObjectMapper objectMapper;

    @Value(value = "${pay.callback.web.host}")
    private String webCallback;

    @Value(value = "${pay.callback.back.host}")
    private String backCallback;

    @Autowired
    PayGateWrapper payGateWrapper;

    public PayAsyncClient() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SuppressWarnings(value = "unchecked")
    public BaseDto<PayFormDataDto> generateFormData(Class<? extends BaseAsyncMapper> baseMapperClass,
                                                    BaseAsyncRequestModel requestModel) throws PayException {
        try {
            ReqData reqData = payGateWrapper.makeReqDataByPost(requestModel.generatePayRequestData());
//            Map field = reqData.getField();
//            requestModel.setSign(reqData.getSign());
//            requestModel.setRequestUrl(reqData.getUrl());
//            requestModel.setRequestData(field.toString());
//            this.createRequest(baseMapperClass, requestModel);
//            logger.info(field);
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(true);
            payFormDataDto.setUrl(reqData.getUrl());
            payFormDataDto.setCode("0000");
            payFormDataDto.setFields(reqData.getField());
            baseDto.setData(payFormDataDto);
            return baseDto;
        } catch (ReqDataException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new PayException(e);
        }
    }

    public BaseCallbackRequestModel parseCallbackRequest(Map<String, String> paramsMap,
                                                         String originalQueryString,
                                                         Class<? extends BaseCallbackMapper> baseMapperClass,
                                                         Class<? extends BaseCallbackRequestModel> callbackRequestModel) {
        try {
            BaseCallbackRequestModel model = parseParamsToModel(paramsMap, callbackRequestModel);
            model.setRequestData(originalQueryString);
            // 生成返回值，并对其加密，然后将model写库
            return this.createCallbackRequest(baseMapperClass, model);
        } catch (VerifyException | IOException e) {
            logger.error(MessageFormat.format("Parse callback request failed: {0}", originalQueryString));
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public BaseCallbackRequestModel parseParamsToModel(Map<String, String> paramsMap, Class<? extends BaseCallbackRequestModel> callbackRequestModel) throws VerifyException, IOException {
        // 解密
        Map<String, String> platNotifyData = payGateWrapper.getPlatNotifyData(paramsMap);
        // aa_bb_cc to aaBbCc
        Map<String, String> newPlatNotifyData = Maps.newHashMap();
        for (String key : platNotifyData.keySet()) {
            StringBuilder newKeyBuilder = new StringBuilder();
            String[] splits = key.split("_");
            for (String split : splits) {
                newKeyBuilder.append(StringUtils.capitalize(split));
            }
            String newKey = StringUtils.uncapitalize(newKeyBuilder.toString());
            newPlatNotifyData.put(newKey, platNotifyData.get(key));
        }

        // Map to Json String
        String json = objectMapper.writeValueAsString(newPlatNotifyData);
        // Json String to model
        return objectMapper.readValue(json, callbackRequestModel);
    }

    @Transactional(value = "payTransactionManager")
    private void createRequest(Class<? extends BaseAsyncMapper> baseMapperClass,
                               BaseAsyncRequestModel requestModel) {
        BaseAsyncMapper mapper = (BaseAsyncMapper) this.getMapperByClass(baseMapperClass);
        mapper.create(requestModel);
    }

    @Transactional(value = "payTransactionManager")
    private BaseCallbackRequestModel createCallbackRequest(Class<? extends BaseCallbackMapper> baseMapperClass,
                                                           BaseCallbackRequestModel requestModel) {
        BaseCallbackMapper mapper = (BaseCallbackMapper) this.getMapperByClass(baseMapperClass);
        requestModel.setResponseTime(new Date());
        Map<String, String> map = requestModel.generatePayResponseData();
        String notifyResData = payGateWrapper.merNotifyResData(map);
        requestModel.setResponseData(notifyResData);
        mapper.create(requestModel);
        return requestModel;
    }

    private Object getMapperByClass(Class clazz) {
        String fullName = clazz.getName();
        String[] strings = fullName.split("\\.");

        String beanName = Introspector.decapitalize(strings[strings.length - 1]);

        return applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PayAsyncClient.applicationContext = applicationContext;
    }
}
