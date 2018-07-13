package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.WithdrawApplyNotifyModel;
import com.tuotiansudai.fudian.ump.asyn.callback.WithdrawNotifyModel;
import com.tuotiansudai.fudian.ump.asyn.request.WithdrawRequestModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UmpWithdrawService {

    private static Logger logger = LoggerFactory.getLogger(UmpWithdrawService.class);

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    @Autowired
    private UmpWithdrawService(InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public WithdrawRequestModel withdraw(String loginName, String payUserId, long withdrawId, long amount) {
        WithdrawRequestModel requestModel = new WithdrawRequestModel(String.valueOf(withdrawId),
                payUserId,
                String.valueOf(amount));

        umpUtils.sign(requestModel);

        insertRequestMapper.insertWithdraw(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP WITHDRAW] failed to sign, data: {}", requestModel);
            return null;
        }
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString) {
        WithdrawNotifyModel withdrawNotifyModel = new WithdrawNotifyModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, withdrawNotifyModel);
        if (Strings.isNullOrEmpty(withdrawNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyWithdraw(withdrawNotifyModel);
        return withdrawNotifyModel.getResponseData();
    }

    public String applyNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        WithdrawApplyNotifyModel withdrawApplyNotifyModel = new WithdrawApplyNotifyModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, withdrawApplyNotifyModel);
        if (Strings.isNullOrEmpty(withdrawApplyNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertApplyNotifyWithdraw(withdrawApplyNotifyModel);
        return withdrawApplyNotifyModel.getResponseData();
    }
}
