package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.RechargeNotifyModel;
import com.tuotiansudai.fudian.ump.asyn.request.RechargeRequestModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UmpRechargeService {

    private static Logger logger = LoggerFactory.getLogger(UmpRechargeService.class);

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    @Autowired
    private UmpRechargeService(InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public RechargeRequestModel recharge(String loginName, String payUserId, boolean isPublicPay, long rechargeId, long amount, String bankCode) {
        RechargeRequestModel model = RechargeRequestModel.newRecharge(String.valueOf(rechargeId),
                payUserId,
                String.valueOf(amount),
                bankCode);

        if (isPublicPay) {
            model = RechargeRequestModel.newFastRecharge(String.valueOf(rechargeId),
                    payUserId,
                    String.valueOf(amount));
        }

        umpUtils.sign(model);

        insertRequestMapper.insertRecharge(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP RECHARGE] failed to sign, data: {}", model);
            return null;
        }
        return model;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        RechargeNotifyModel model = new RechargeNotifyModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, model);

        if (Strings.isNullOrEmpty(model.getResponseData())){
            return null;
        }

        insertNotifyMapper.insertNotifyRecharge(model);
        return model.getResponseData();
    }

}
