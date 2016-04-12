package com.tuotiansudai.paywrapper.client.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.paywrapper.client.PayGateWrapper;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ParameterCheckException;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.util.DataUtil;
import com.umpay.api.util.HttpMerParserUtil;
import com.umpay.api.util.PlainUtil;

import java.util.Map;

public class PayGateWrapperFakeImpl extends PayGateWrapper {
    private String fakeServiceUrl;

    public PayGateWrapperFakeImpl(String fakeServiceUrlPrefix) {
        this.fakeServiceUrl = fakeServiceUrlPrefix + "/spay/pay/payservice.do";
    }

    public void setFakeServiceUrl(String fakeServiceUrl) {
        this.fakeServiceUrl = fakeServiceUrl;
    }

    @Override
    public Map<String, String> getPlatNotifyData(Map<String, String> paramsMap) {
        return DataUtil.getData(paramsMap);
    }

    @Override
    public Map<String, String> getResData(String responseBodyString) throws RetDataException {
        String content = HttpMerParserUtil.getMeta(responseBodyString);
        return PlainUtil.getResPlain(content);
    }

    @Override
    public ReqData makeReqDataByPost(Object obj) throws ReqDataException {
        ReqData reqData = Mer2Plat_v40.makeReqDataByPost(obj);
        if (!Strings.isNullOrEmpty(fakeServiceUrl)) {
            reqData.setUrl(fakeServiceUrl);
        }
        return reqData;
    }

    @Override
    public String merNotifyResData(Object map) throws ParameterCheckException {
        return Mer2Plat_v40.merNotifyResData(map);
    }
}
