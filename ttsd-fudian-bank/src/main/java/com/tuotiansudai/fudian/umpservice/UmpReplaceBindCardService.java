package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.BankCardApplyNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.BankCardNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.PtpMerReplaceCardRequestModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UmpReplaceBindCardService {

    private static Logger logger = LoggerFactory.getLogger(UmpReplaceBindCardService.class);

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    @Autowired
    private UmpReplaceBindCardService(InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public PtpMerReplaceCardRequestModel replaceBindCard(String loginName, long bankCardModelId, String payUserId, String cardNumber, String userName, String identityNumber) {
        PtpMerReplaceCardRequestModel requestModel = new PtpMerReplaceCardRequestModel(String.valueOf(bankCardModelId),
                cardNumber,
                payUserId,
                userName,
                identityNumber);

        umpUtils.sign(requestModel);

        insertRequestMapper.insertReplaceCardBind(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP REPLACE BIND CARD] failed to sign, data: {}", requestModel);
            return null;
        }
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        BankCardNotifyRequestModel bindCardNotifyModel = new BankCardNotifyRequestModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, bindCardNotifyModel);
        if (Strings.isNullOrEmpty(bindCardNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyCardBind(bindCardNotifyModel);
        return bindCardNotifyModel.getResponseData();
    }

    public String applyNotifyCallBack(Map<String, String> paramsMap, String queryString){
        BankCardApplyNotifyRequestModel bindCardApplyNotifyModel = new BankCardApplyNotifyRequestModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, bindCardApplyNotifyModel);
        if (Strings.isNullOrEmpty(bindCardApplyNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertApplyNotifyCardBind(bindCardApplyNotifyModel);
        return bindCardApplyNotifyModel.getResponseData();
    }
}
