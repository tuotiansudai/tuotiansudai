package com.tuotiansudai.paywrapper.client;

import com.google.common.base.Strings;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ParameterCheckException;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.util.DataUtil;
import com.umpay.api.util.HttpMerParserUtil;
import com.umpay.api.util.PlainUtil;

import java.util.Map;

public class MockPayGateWrapper extends PayGateWrapper {

    private static final MockPayGateWrapper instance = new MockPayGateWrapper();

    private MockPayGateWrapper() {
    }

    private String requestUrl;

    public static void inject(PaySyncClient paySyncClient) {
        paySyncClient.payGateWrapper = instance;
    }

    public static void inject(PayAsyncClient payAsyncClient) {
        payAsyncClient.payGateWrapper = instance;
    }

    public static void setUrl(String requestUrl) {
        instance.requestUrl = requestUrl;
    }

    @Override
    public Map<String, String> getPlatNotifyData(Map<String, String> paramsMap) throws VerifyException {
        return DataUtil.getData(paramsMap);
    }

    @Override
    public Map<String, String> getResData(String responseBodyString) throws RetDataException {
        String content = HttpMerParserUtil.getMeta(responseBodyString);
        return PlainUtil.getResPlain(content);
    }

    @Override
    public ReqData makeReqDataByPost(Object obj) throws ReqDataException {
        ReqData reqData = super.makeReqDataByPost(obj);
        if (!Strings.isNullOrEmpty(requestUrl)) {
            reqData.setUrl(requestUrl);
        }
        return reqData;
    }

    @Override
    public String merNotifyResData(Object map) throws ParameterCheckException {
        return super.merNotifyResData(map);
    }
}
