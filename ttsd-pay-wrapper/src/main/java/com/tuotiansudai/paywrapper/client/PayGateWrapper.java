package com.tuotiansudai.paywrapper.client;

import com.tuotiansudai.paywrapper.client.impl.PayGateWrapperFakeImpl;
import com.tuotiansudai.paywrapper.client.impl.PayGateWrapperImpl;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ParameterCheckException;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PayGateWrapper implements InitializingBean {

    @Value(value = "${ump.service.fake}")
    private boolean umpServiceFaked;

    @Value(value = "${ump.service.fake.url}")
    private String umpServiceFakeUrlPrefix;

    private PayGateWrapper payGateWrapperImpl;

    public Map<String, String> getPlatNotifyData(Map<String, String> paramsMap) throws VerifyException {
        return payGateWrapperImpl.getPlatNotifyData(paramsMap);
    }

    public Map<String, String> getResData(String responseBodyString) throws RetDataException {
        return payGateWrapperImpl.getResData(responseBodyString);
    }

    public ReqData makeReqDataByPost(Object obj) throws ReqDataException {
        return payGateWrapperImpl.makeReqDataByPost(obj);
    }

    public String merNotifyResData(Object map) throws ParameterCheckException {
        return payGateWrapperImpl.merNotifyResData(map);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (umpServiceFaked) {
            payGateWrapperImpl = new PayGateWrapperFakeImpl(umpServiceFakeUrlPrefix);
        } else {
            payGateWrapperImpl = new PayGateWrapperImpl();
        }
    }
}
