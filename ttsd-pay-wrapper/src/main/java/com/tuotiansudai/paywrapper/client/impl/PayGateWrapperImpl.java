package com.tuotiansudai.paywrapper.client.impl;

import com.tuotiansudai.paywrapper.client.PayGateWrapper;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ParameterCheckException;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

import java.util.Map;

public class PayGateWrapperImpl extends PayGateWrapper {

    public Map<String, String> getPlatNotifyData(Map<String, String> paramsMap) throws VerifyException {
        return Plat2Mer_v40.getPlatNotifyData(paramsMap);
    }

    public Map<String, String> getResData(String responseBodyString) throws RetDataException {
        return Plat2Mer_v40.getResData(responseBodyString);
    }

    public ReqData makeReqDataByPost(Object obj) throws ReqDataException {
        return Mer2Plat_v40.makeReqDataByPost(obj);
    }

    public String merNotifyResData(Object map) throws ParameterCheckException {
        return Mer2Plat_v40.merNotifyResData(map);
    }
}
