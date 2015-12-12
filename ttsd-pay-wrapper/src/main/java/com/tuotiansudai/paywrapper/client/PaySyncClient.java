package com.tuotiansudai.paywrapper.client;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.BaseSyncMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.util.SpringContextUtil;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.io.IOException;
import java.util.Map;

@Component
public class PaySyncClient {

    static Logger logger = Logger.getLogger(PaySyncClient.class);

    @Autowired
    private OkHttpClient httpClient;

    @Autowired
    PayGateWrapper payGateWrapper;

    @SuppressWarnings(value = "unchecked")
    public <T extends BaseSyncResponseModel> T send(Class<? extends BaseSyncMapper> baseMapperClass, BaseSyncRequestModel requestModel, Class<T> responseModelClass) throws PayException {
        ReqData reqData;
        try {
            reqData = payGateWrapper.makeReqDataByPost(requestModel.generatePayRequestData());
            requestModel.setSign(reqData.getSign());
            requestModel.setRequestData(reqData.getField().toString());
            requestModel.setRequestUrl(reqData.getUrl());
            logger.debug(reqData.getField());
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
        Request request = new Request.Builder().url(reqData.getUrl()).post(formEncodingBuilder.build()).build();

        String responseBodyString;
        try {
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.SENT);
            Response response = httpClient.newCall(request).execute();
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.SUCCESS);
            responseBodyString = response.body().string();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            updateRequestStatus(baseMapperClass, requestModel.getId(), SyncRequestStatus.FAILURE);
            throw new PayException(e);
        }

        try {
            Map<String, String> resData = payGateWrapper.getResData(responseBodyString);
            logger.debug(resData);
            return createResponse(baseMapperClass, resData, responseModelClass, requestModel.getId());
        } catch (RetDataException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new PayException(e);
        }
    }

    @Transactional(value = "payTransactionManager")
    private void createRequest(Class<? extends BaseSyncMapper> baseMapperClass, BaseSyncRequestModel requestModel) {
        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.createRequest(requestModel);
    }

    @Transactional(value = "payTransactionManager")
    private void updateRequestStatus(Class<? extends BaseSyncMapper> baseMapperClass,
                                     Long id,
                                     SyncRequestStatus status) {
        BaseSyncMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.updateRequestStatus(id, status);
    }

    @Transactional(value = "payTransactionManager")
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

        return (BaseSyncMapper) SpringContextUtil.getBeanByName(beanName);
    }

}
