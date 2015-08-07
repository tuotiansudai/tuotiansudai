package com.tuotiansudai.paywrapper.client;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.paywrapper.repository.model.request.BaseRequestModel;
import com.tuotiansudai.paywrapper.repository.model.request.RequestStatus;
import com.tuotiansudai.paywrapper.repository.model.response.BaseResponseModel;
import com.tuotiansudai.paywrapper.util.SpringContextUtil;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.io.IOException;
import java.util.Map;


@Component
public class PayClient {

    static Logger logger = Logger.getLogger(PayClient.class);

    @Autowired
    private OkHttpClient httpClient;

    public <T extends BaseResponseModel> T send(Class<? extends BaseMapper> baseMapperClass, BaseRequestModel requestModel, Class<T> responseModelClass) throws PayException {
        ReqData reqData;
        try {
            reqData = Mer2Plat_v40.makeReqDataByPost(requestModel.generatePayRequestData());
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
            Response response = httpClient.newCall(request).execute();
            responseBodyString = response.body().string();
            updateRequestStatus(baseMapperClass, requestModel.getId(), RequestStatus.SUCCESS);
            Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyString);
            logger.debug(resData);
            return createResponse(baseMapperClass, resData, responseModelClass, requestModel.getId());
        } catch (RetDataException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new PayException(e);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            updateRequestStatus(baseMapperClass, requestModel.getId(), RequestStatus.FAILED);
            throw new PayException(e);
        }
    }

    @Transactional(value = "payTransactionManager")
     private void createRequest(Class<? extends BaseMapper> baseMapperClass, BaseRequestModel requestModel) {
        BaseMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.createRequest(requestModel);
    }

    @Transactional(value = "payTransactionManager")
    private void updateRequestStatus(Class<? extends BaseMapper> baseMapperClass,
                                     Long id,
                                     RequestStatus status) {
        BaseMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.updateRequestStatus(id, status);
    }

    @Transactional(value = "payTransactionManager")
    private <T extends BaseResponseModel> T createResponse(Class<? extends BaseMapper> baseMapperClass,
                                                           Map<String, String> resData,
                                                           Class<T> responseModelClass,
                                                           Long requestId) throws IllegalAccessException, InstantiationException {
        BaseMapper mapper = this.getMapperByClass(baseMapperClass);
        T responseModel = responseModelClass.newInstance();
        responseModel.setRequestId(requestId);
        responseModel.initializeModel(resData);
        mapper.createResponse(responseModel);
        return responseModel;
    }

    private BaseMapper getMapperByClass(Class clazz) {
        String fullName = clazz.getName();
        String[] strings = fullName.split("\\.");

        String beanName = Introspector.decapitalize(strings[strings.length - 1]);

        return (BaseMapper) SpringContextUtil.getBeanByName(beanName);
    }

}
