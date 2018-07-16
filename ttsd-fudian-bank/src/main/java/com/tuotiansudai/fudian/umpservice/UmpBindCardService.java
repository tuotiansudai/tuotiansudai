package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.BankCardApplyNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.BankCardNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.PtpMerBindCardRequestModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UmpBindCardService {

    private static Logger logger = LoggerFactory.getLogger(UmpBindCardService.class);

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    @Autowired
    private UmpBindCardService(InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public PtpMerBindCardRequestModel bindCard(String loginName, long bankCardModelId, String payUserId, String cardNumber, String userName, String identityNumber, boolean isFastPay) {
        PtpMerBindCardRequestModel requestModel = new PtpMerBindCardRequestModel(String.valueOf(bankCardModelId),
                cardNumber,
                payUserId,
                userName,
                identityNumber,
                isFastPay);

        umpUtils.sign(requestModel);

        insertRequestMapper.insertCardBind(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP BIND CARD] failed to sign, data: {}", requestModel);
            return null;
        }
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        BankCardNotifyRequestModel bindCardNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, new BankCardNotifyRequestModel());
        if (Strings.isNullOrEmpty(bindCardNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyCardBind(bindCardNotifyModel);
        return bindCardNotifyModel.getResponseData();
    }

    public String applyNotifyCallBack(Map<String, String> paramsMap, String queryString){
        BankCardApplyNotifyRequestModel bindCardApplyNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, new BankCardApplyNotifyRequestModel());
        if (Strings.isNullOrEmpty(bindCardApplyNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertApplyNotifyCardBind(bindCardApplyNotifyModel);
        return bindCardApplyNotifyModel.getResponseData();
    }
}
