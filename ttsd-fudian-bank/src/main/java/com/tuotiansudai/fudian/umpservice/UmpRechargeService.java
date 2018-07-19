package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.RechargeNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.MerRechargePersonRequestModel;
import com.tuotiansudai.fudian.umpdto.UmpRechargeDto;
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

    public MerRechargePersonRequestModel recharge(UmpRechargeDto dto) {
        MerRechargePersonRequestModel model = MerRechargePersonRequestModel.newRecharge(String.valueOf(dto.getRechargeId()),
                dto.getPayUserId(),
                String.valueOf(dto.getAmount()),
                dto.getBankCode());

        if (dto.isFastPay()) {
            model = MerRechargePersonRequestModel.newFastRecharge(String.valueOf(dto.getRechargeId()),
                    dto.getPayUserId(),
                    String.valueOf(dto.getAmount()));
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
        RechargeNotifyRequestModel model = umpUtils.parseCallbackRequest(paramsMap, queryString, new RechargeNotifyRequestModel());
        if (Strings.isNullOrEmpty(model.getResponseData())){
            return null;
        }
        insertNotifyMapper.insertNotifyRecharge(model);
        return model.getResponseData();
    }

}
