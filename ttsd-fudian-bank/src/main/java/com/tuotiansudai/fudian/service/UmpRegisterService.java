package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.ump.sync.request.RegisterRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.RegisterResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class UmpRegisterService {

    private static Logger logger = LoggerFactory.getLogger(UmpRegisterService.class);

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateMapper updateMapper;

    private final InsertResponseMapper insertResponseMapper;

    private final UmpUtils umpUtils;

    @Autowired
    public UmpRegisterService(InsertRequestMapper insertRequestMapper, UpdateMapper updateMapper, InsertResponseMapper insertResponseMapper, UmpUtils umpUtils){
        this.insertRequestMapper = insertRequestMapper;
        this.updateMapper = updateMapper;
        this.insertResponseMapper = insertResponseMapper;
        this.umpUtils = umpUtils;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage register(String loginName, String mobile, String userName, String identityNumber){
        RegisterRequestModel model = new RegisterRequestModel(
                String.valueOf(new Date().getTime()),
                loginName,
                userName,
                identityNumber,
                mobile);

        umpUtils.sign(model);

        insertRequestMapper.insertRegister(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP register] failed to sign, data: {}", model);
            return new BankBaseMessage(false, "签名失败");
        }

        updateMapper.updateRegister(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null){
            updateMapper.updateRegister(SyncRequestStatus.FAILURE, model.getId());
            return new BankBaseMessage(false, "请求联动优势失败");
        }

        updateMapper.updateRegister(SyncRequestStatus.SUCCESS, model.getId());
        RegisterResponseModel responseModel = new RegisterResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);
        insertResponseMapper.insertResponseRegister(responseModel);
        return new BankBaseMessage(true, null);

    }
}
