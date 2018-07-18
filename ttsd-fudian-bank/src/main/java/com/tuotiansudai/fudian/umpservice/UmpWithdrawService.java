package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.WithdrawApplyNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.WithdrawNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.CustWithdrawalsRequestModel;
import com.tuotiansudai.fudian.umpdto.UmpWithdrawDto;
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

    public CustWithdrawalsRequestModel withdraw(UmpWithdrawDto dto) {
        CustWithdrawalsRequestModel requestModel = new CustWithdrawalsRequestModel(String.valueOf(dto.getWithdrawId()),
                dto.getPayUserId(),
                String.valueOf(dto.getAmount()));

        umpUtils.sign(requestModel);

        insertRequestMapper.insertWithdraw(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP WITHDRAW] failed to sign, data: {}", requestModel);
            return null;
        }
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString) {
        WithdrawNotifyRequestModel withdrawNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, new WithdrawNotifyRequestModel());
        if (Strings.isNullOrEmpty(withdrawNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyWithdraw(withdrawNotifyModel);
        return withdrawNotifyModel.getResponseData();
    }

    public String applyNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        WithdrawApplyNotifyRequestModel withdrawApplyNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, new WithdrawApplyNotifyRequestModel());
        if (Strings.isNullOrEmpty(withdrawApplyNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertApplyNotifyWithdraw(withdrawApplyNotifyModel);
        return withdrawApplyNotifyModel.getResponseData();
    }
}
