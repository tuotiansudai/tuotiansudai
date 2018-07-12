package com.tuotiansudai.fudian.umpClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.dto.umpRequest.*;
import com.tuotiansudai.fudian.dto.umpResponse.BaseNotifyModel;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Component
public class PayAsyncClient {

    private static Logger logger = Logger.getLogger(PayAsyncClient.class);

    private ObjectMapper objectMapper;

    @Value(value = "${pay.callback.web.host}")
    private String webCallback;

    @Value(value = "${pay.callback.back.host}")
    private String backCallback;

    private final PayGateWrapper payGateWrapper;

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    public PayAsyncClient(PayGateWrapper payGateWrapper, InsertRequestMapper insertRequestMapper, InsertNotifyMapper insertNotifyMapper) {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.payGateWrapper = payGateWrapper;
        this.insertRequestMapper = insertRequestMapper;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends BaseAsyncRequestModel> UmpAsyncMessage generateFormData(T requestModel, AsyncUmPayService service){
        try {
            ReqData reqData = payGateWrapper.makeReqDataByPost(requestModel.generatePayRequestData());
            Map field = reqData.getField();
            requestModel.setSign(reqData.getSign());
            requestModel.setRequestUrl(reqData.getUrl());
            requestModel.setRequestData(field.toString());

            this.insertRequestModel(requestModel, service);

            return new UmpAsyncMessage(true, reqData.getUrl(), reqData.getField(), "0000");
        } catch (ReqDataException e) {
            logger.error("[UMP] generate form data fail:{}", e);
        }
        return new UmpAsyncMessage(false, null, null, "请求数据生成失败");
    }


    public BaseNotifyModel parseCallbackRequest(Map<String, String> paramsMap,
                                                String originalQueryString,
                                                Class<? extends BaseCallbackMapper> baseMapperClass,
                                                Class<? extends BaseNotifyModel> callbackRequestModel) {
        try {
            BaseNotifyModel model = parseParamsToModel(paramsMap, callbackRequestModel);
            model.setRequestData(originalQueryString);
            // 生成返回值，并对其加密，然后将model写库
            return this.createCallbackRequest(baseMapperClass, model);
        } catch (VerifyException | IOException e) {
            logger.error(MessageFormat.format("Parse callback request failed: {0}", originalQueryString));
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private BaseNotifyModel parseParamsToModel(Map<String, String> paramsMap, Class<? extends BaseNotifyModel> callbackRequestModel) throws VerifyException, IOException {
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
    private BaseNotifyModel createCallbackRequest(Class<? extends BaseCallbackMapper> baseMapperClass,
                                                  BaseNotifyModel requestModel) {
        BaseCallbackMapper mapper = (BaseCallbackMapper) this.getMapperByClass(baseMapperClass);
        requestModel.setResponseTime(new Date());
        Map<String, String> map = requestModel.generatePayResponseData();
        String notifyResData = payGateWrapper.merNotifyResData(map);
        requestModel.setResponseData(notifyResData);
        mapper.create(requestModel);
        return requestModel;
    }
//
//    private Object getMapperByClass(Class clazz) {
//        String fullName = clazz.getName();
//        String[] strings = fullName.split("\\.");
//
//        String beanName = Introspector.decapitalize(strings[strings.length - 1]);
//
//        return applicationContext.getBean(beanName);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        PayAsyncClient.applicationContext = applicationContext;
//    }


    private <T extends BaseAsyncRequestModel> void insertRequestModel(T requestModel, AsyncUmPayService service){
        Maps.newHashMap(ImmutableMap.<AsyncUmPayService, Runnable>builder()
                .put(AsyncUmPayService.PTP_MER_BIND_CARD, () -> insertRequestMapper.insertCardBind((BindCardRequestModel) requestModel))
                .put(AsyncUmPayService.PTP_MER_REPLACE_CARD, () -> insertRequestMapper.insertReplaceCardBind((ReplaceCardRequestModel) requestModel))
                .put(AsyncUmPayService.MER_RECHARGE_PERSON, () -> insertRequestMapper.insertRecharge((RechargeRequestModel) requestModel))
                .put(AsyncUmPayService.CUST_WITHDRAWALS, () -> insertRequestMapper.insertWithdraw((WithdrawRequestModel) requestModel))
                .put(AsyncUmPayService.NORMAL_REPAY_PROJECT_TRANSFER, () -> insertRequestMapper.insertLoanRepay((LoanRepayRequestModel) requestModel))
                .put(AsyncUmPayService.ADVANCE_REPAY_PROJECT_TRANSFER, () -> insertRequestMapper.insertLoanRepay((LoanRepayRequestModel) requestModel))
                .build()).get(service).run();
    }

    private <T extends BaseNotifyModel> void insertNotifyModel(T notifyModel, AsyncUmPayService service){
        Maps.newHashMap(ImmutableMap.<AsyncUmPayService, Runnable>builder()
                .put(AsyncUmPayService.PTP_MER_BIND_CARD, () -> insertNotifyMapper.insertCardBind((BindCardRequestModel) requestModel))
                .put(AsyncUmPayService.PTP_MER_REPLACE_CARD, () -> insertNotifyMapper.insertReplaceCardBind((ReplaceCardRequestModel) requestModel))
                .put(AsyncUmPayService.MER_RECHARGE_PERSON, () -> insertNotifyMapper.insertRecharge((RechargeRequestModel) requestModel))
                .put(AsyncUmPayService.CUST_WITHDRAWALS, () -> insertNotifyMapper.insertWithdraw((WithdrawRequestModel) requestModel))
                .put(AsyncUmPayService.NORMAL_REPAY_PROJECT_TRANSFER, () -> insertNotifyMapper.insertLoanRepay((LoanRepayRequestModel) requestModel))
                .put(AsyncUmPayService.ADVANCE_REPAY_PROJECT_TRANSFER, () -> insertNotifyMapper.insertLoanRepay((LoanRepayRequestModel) requestModel))
                .build()).get(service).run();
    }
}
