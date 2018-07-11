package com.tuotiansudai.fudian.umpClient;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.enums.SyncUmPayService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.exception.PayTimeoutException;
import com.tuotiansudai.paywrapper.repository.mapper.BaseSyncMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PaySyncClient implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    static Logger logger = Logger.getLogger(PaySyncClient.class);

    private OkHttpClient commonHttpClient;
    private OkHttpClient merRegisterPersonHttpClient;

    @Autowired
    PayGateWrapper payGateWrapper;

    public PaySyncClient() {
        this.commonHttpClient = buildHttpClient(10, TimeUnit.SECONDS);
        this.merRegisterPersonHttpClient = buildHttpClient(15, TimeUnit.SECONDS);
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends BaseSyncResponseModel> T send(Class<? extends BaseSyncMapper> baseMapperClass, BaseSyncRequestModel requestModel, Class<T> responseModelClass) throws PayException {
        ReqData reqData;
        try {
            reqData = payGateWrapper.makeReqDataByPost(requestModel.generatePayRequestData());
            requestModel.setSign(reqData.getSign());
            requestModel.setRequestData(reqData.getField().toString());
            requestModel.setRequestUrl(reqData.getUrl());
            logger.info(reqData.getField());
            createRequest(baseMapperClass, requestModel);
        } catch (ReqDataException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new PayException(e);
        }

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        Map<String, String> field = (Map<String, String>) reqData.getField();
        for (String key : field.keySet()) {
            String value = field.get(key);
            formEncodingBuilder.add(key, value);
        }

        logger.info(reqData.getUrl());
        Request request = new Request.Builder().url(reqData.getUrl()).post(formEncodingBuilder.build()).build();

        String responseBodyString;
        try {
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.SENT);
            Response response = selectHttpClient(requestModel.getService()).newCall(request).execute();
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.SUCCESS);
            responseBodyString = response.body().string();
        } catch (SocketTimeoutException e) {
            logger.error(e.getLocalizedMessage(), e);
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.FAILURE);
            throw new PayTimeoutException(e);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.FAILURE);
            throw new PayException(e);
        }

        try {
            Map<String, String> resData = payGateWrapper.getResData(responseBodyString);
            logger.info(resData);
            return createResponse(baseMapperClass, resData, responseModelClass, requestModel.getId());
        } catch (RetDataException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new PayException(e);
        }
    }

    private void createRequest(Class<? extends BaseSyncMapper> baseMapperClass, BaseSyncRequestModel requestModel) {
        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.createRequest(requestModel);
    }

    private void updateRequestStatus(Class<? extends BaseSyncMapper> baseMapperClass,
                                     Long id,
                                     SyncRequestStatus status) {
        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.updateRequestStatus(id, status);
    }

    private <T extends BaseSyncResponseModel> T createResponse(Class<? extends BaseSyncMapper> baseMapperClass,
                                                               Map<String, String> resData,
                                                               Class<T> responseModelClass,
                                                               Long requestId) throws IllegalAccessException, InstantiationException {
        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
        T responseModel = responseModelClass.newInstance();
        responseModel.setRequestId(requestId);
        responseModel.initializeModel(resData);
        mapper.createResponse(responseModel);
        return responseModel;
    }

    private BaseSyncMapper getMapperByClass(Class clazz) {
        String fullName = clazz.getName();
        String[] strings = fullName.split("\\.");

        String beanName = Introspector.decapitalize(strings[strings.length - 1]);

        return (BaseSyncMapper) applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PaySyncClient.applicationContext = applicationContext;
    }

    private OkHttpClient buildHttpClient(long timeout, TimeUnit unit) {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(timeout, unit);
        httpClient.setReadTimeout(timeout, unit);
        httpClient.setWriteTimeout(timeout, unit);
        return httpClient;
    }

    public OkHttpClient selectHttpClient(String service){
        if (SyncUmPayService.MER_REGISTER_PERSON.getServiceName().equalsIgnoreCase(service)) {
            return merRegisterPersonHttpClient;
        } else {
            return commonHttpClient;
        }
    }
}
