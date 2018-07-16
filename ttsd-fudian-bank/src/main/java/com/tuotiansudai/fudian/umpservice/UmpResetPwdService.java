package com.tuotiansudai.fudian.umpservice;

import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.ump.sync.request.MerSendSmsPwdRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.MerSendSmsPwdResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class UmpResetPwdService {

    private static Logger logger = LoggerFactory.getLogger(UmpResetPwdService.class);

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateMapper updateMapper;

    private final InsertResponseMapper insertResponseMapper;

    private final UmpUtils umpUtils;

    @Autowired
    public UmpResetPwdService(InsertRequestMapper insertRequestMapper, UpdateMapper updateMapper, InsertResponseMapper insertResponseMapper, UmpUtils umpUtils){
        this.insertRequestMapper = insertRequestMapper;
        this.updateMapper = updateMapper;
        this.insertResponseMapper = insertResponseMapper;
        this.umpUtils = umpUtils;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage reset(String loginName, String payUserId, String identityNumber){
        MerSendSmsPwdRequestModel model = new MerSendSmsPwdRequestModel(payUserId,
                identityNumber,
                String.valueOf(new Date().getTime()));

        umpUtils.sign(model);

        insertRequestMapper.insertResetPwd(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP reset] failed to sign, data: {}", model);
            return new BankBaseMessage(false, "签名失败");
        }

        updateMapper.updateResetPwd(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null){
            updateMapper.updateRegister(SyncRequestStatus.FAILURE, model.getId());
            return new BankBaseMessage(false, "请求联动优势失败");
        }

        updateMapper.updateResetPwd(SyncRequestStatus.SUCCESS, model.getId());
        MerSendSmsPwdResponseModel responseModel = new MerSendSmsPwdResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);
        insertResponseMapper.insertResponseResetPwd(responseModel);
        return new BankBaseMessage(true, null);

    }
}
