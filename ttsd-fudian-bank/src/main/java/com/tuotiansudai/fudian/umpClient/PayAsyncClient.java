package com.tuotiansudai.fudian.umpClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.ump.AsyncUmPayService;
import com.tuotiansudai.fudian.ump.asyn.callback.BaseNotifyModel;
import com.tuotiansudai.fudian.ump.asyn.request.BaseAsyncRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.*;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class PayAsyncClient {

    private static Logger logger = Logger.getLogger(PayAsyncClient.class);

    private ObjectMapper objectMapper;

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

    private <T extends BaseAsyncRequestModel> void insertRequestModel(T requestModel, AsyncUmPayService service){
        Maps.newHashMap(ImmutableMap.<AsyncUmPayService, Runnable>builder()
                .put(AsyncUmPayService.PTP_MER_BIND_CARD, () -> insertRequestMapper.insertCardBind((BindCardRequestModel) requestModel))
                .put(AsyncUmPayService.PTP_MER_REPLACE_CARD, () -> insertRequestMapper.insertReplaceCardBind((ReplaceCardRequestModel) requestModel))
                .put(AsyncUmPayService.MER_RECHARGE_PERSON, () -> insertRequestMapper.insertRecharge((RechargeRequestModel) requestModel))
                .put(AsyncUmPayService.CUST_WITHDRAWALS, () -> insertRequestMapper.insertWithdraw((WithdrawRequestModel) requestModel))
                .put(AsyncUmPayService.NORMAL_REPAY_PROJECT_TRANSFER, () -> insertRequestMapper.insertProjectTransfer((ProjectTransferRequestModel) requestModel))
                .put(AsyncUmPayService.ADVANCE_REPAY_PROJECT_TRANSFER, () -> insertRequestMapper.insertProjectTransfer((ProjectTransferRequestModel) requestModel))
                .build()).get(service).run();
    }

//    public BaseNotifyModel parseCallbackRequest(Map<String, String> paramsMap,
//                                                String originalQueryString,
//                                                Class<? extends BaseCallbackMapper> baseMapperClass,
//                                                Class<? extends BaseNotifyModel> callbackRequestModel) {
//        try {
//            BaseNotifyModel model = parseParamsToModel(paramsMap, callbackRequestModel);
//            model.setRequestData(originalQueryString);
//            // 生成返回值，并对其加密，然后将model写库
//            return this.createCallbackRequest(baseMapperClass, model);
//        } catch (VerifyException | IOException e) {
//            logger.error(MessageFormat.format("Parse callback request failed: {0}", originalQueryString));
//            logger.error(e.getLocalizedMessage(), e);
//        }
//        return null;
//    }

    public BaseNotifyModel parseParamsToModel(Map<String, String> paramsMap, Class<? extends BaseNotifyModel> callbackRequestModel) throws VerifyException, IOException {
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
}
