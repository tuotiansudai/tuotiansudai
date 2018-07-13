package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.ump.asyn.callback.TransferNotifyRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.request.TransferRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.TransferResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class UmpExperienceRepayService {

    private static Logger logger = LoggerFactory.getLogger(UmpExperienceRepayService.class);

    private final static String EXPERIENCE_ORDER_ID_TEMPLATE = "{0}X{1}";

    private final UmpUtils umpUtils;

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateMapper updateMapper;

    private final InsertResponseMapper insertResponseMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    @Autowired
    public UmpExperienceRepayService(UmpUtils umpUtils, InsertRequestMapper insertRequestMapper, UpdateMapper updateMapper, InsertResponseMapper insertResponseMapper, InsertNotifyMapper insertNotifyMapper){
        this.umpUtils = umpUtils;
        this.insertRequestMapper = insertRequestMapper;
        this.updateMapper = updateMapper;
        this.insertResponseMapper = insertResponseMapper;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage experienceRepay(long investRepayModelId, String payUserId, String payAccountId, long amount){
        TransferRequestModel model = TransferRequestModel.experienceInterestRequest(
                MessageFormat.format(EXPERIENCE_ORDER_ID_TEMPLATE, String.valueOf(investRepayModelId), String.valueOf(new Date().getTime())),
                payUserId,
                payAccountId,
                String.valueOf(amount));

        umpUtils.sign(model);

        insertRequestMapper.insertTransfer(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP COUPON REPAY] failed to sign, data: {}", model);
            return new BankBaseMessage(false, "签名失败");
        }

        updateMapper.updateTransfer(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null){
            updateMapper.updateTransfer(SyncRequestStatus.FAILURE, model.getId());
            return new BankBaseMessage(false, "请求联动优势失败");
        }

        updateMapper.updateTransfer(SyncRequestStatus.SUCCESS, model.getId());
        TransferResponseModel responseModel = new TransferResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);
        insertResponseMapper.insertResponseTransfer(responseModel);
        return new BankBaseMessage(true, null);

    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        TransferNotifyRequestModel transferNotifyRequestModel = new TransferNotifyRequestModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, transferNotifyRequestModel);
        if (Strings.isNullOrEmpty(transferNotifyRequestModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyExperience(transferNotifyRequestModel);
        return transferNotifyRequestModel.getResponseData();
    }
}
