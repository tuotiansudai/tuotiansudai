package com.tuotiansudai.fudian.umpClient;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.AsyncUmPayService;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.TransferRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.BaseSyncResponseModel;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PaySyncClient{

    static Logger logger = Logger.getLogger(PaySyncClient.class);

    private static OkHttpClient client;

    private final PayGateWrapper payGateWrapper;

    private final InsertRequestMapper insertRequestMapper;

    public PaySyncClient(PayGateWrapper payGateWrapper, InsertRequestMapper insertRequestMapper) {
        client = new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.payGateWrapper = payGateWrapper;
        this.insertRequestMapper = insertRequestMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends BaseSyncResponseModel> T send(BaseSyncRequestModel requestModel, AsyncUmPayService syncUmPayService){
        ReqData reqData;
        try {
            reqData = payGateWrapper.makeReqDataByPost(requestModel.generatePayRequestData());
            requestModel.setSign(reqData.getSign());
            requestModel.setRequestData(reqData.getField().toString());
            requestModel.setRequestUrl(reqData.getUrl());
            logger.info(reqData.getField());

            insertRequestModel(requestModel, syncUmPayService);

        } catch (ReqDataException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }

        Map<String, String> field = (Map<String, String>) reqData.getField();
        FormBody.Builder formBody = new FormBody.Builder();
        for (String key : field.keySet()) {
            formBody.add(key, field.get(key));
        }

        Request request = new Request.Builder().url(reqData.getUrl()).post(formBody.build()).build();

        String responseBodyString;
        try {
//            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.SENT);
            Response response = client.newCall(request).execute();
//            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.SUCCESS);
            responseBodyString = response.body().string();
        } catch (SocketTimeoutException e) {
            logger.error(e.getLocalizedMessage(), e);
//            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.FAILURE);
            return null;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
//            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.FAILURE);
            return null;
        }

        try {
            Map<String, String> resData = payGateWrapper.getResData(responseBodyString);
            logger.info(resData);
//            return createResponse(baseMapperClass, resData, responseModelClass, requestModel.getId());
        } catch (RetDataException e) {
            logger.error(e.getLocalizedMessage(), e);
//            throw new PayException(e);
        }
        return null;
    }

    private <T extends BaseSyncRequestModel> void insertRequestModel(T requestModel, AsyncUmPayService service){
        Maps.newHashMap(ImmutableMap.<AsyncUmPayService, Runnable>builder()
                .put(AsyncUmPayService.COUPON_REPAY_TRANSFER, () -> insertRequestMapper.insertCouponRepay((TransferRequestModel) requestModel))
                .put(AsyncUmPayService.EXTRA_RATE_TRANSFER, () -> insertRequestMapper.insertExtraRate((TransferRequestModel) requestModel))
                .build()).get(service).run();
    }

//    private void updateRequestStatus(Class<? extends BaseSyncMapper> baseMapperClass,
//                                     Long id,
//                                     SyncRequestStatus status) {
//        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
//        mapper.updateRequestStatus(id, status);
//    }

//    private <T extends BaseSyncResponseModel> T createResponse(Class<? extends BaseSyncMapper> baseMapperClass,
//                                                               Map<String, String> resData,
//                                                               Class<T> responseModelClass,
//                                                               Long requestId) throws IllegalAccessException, InstantiationException {
//        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
//        T responseModel = responseModelClass.newInstance();
//        responseModel.setRequestId(requestId);
//        responseModel.initializeModel(resData);
//        mapper.createResponse(responseModel);
//        return responseModel;
//    }

//    private BaseSyncMapper getMapperByClass(Class clazz) {
//        String fullName = clazz.getName();
//        String[] strings = fullName.split("\\.");
//
//        String beanName = Introspector.decapitalize(strings[strings.length - 1]);
//
//        return (BaseSyncMapper) applicationContext.getBean(beanName);
//    }
}
