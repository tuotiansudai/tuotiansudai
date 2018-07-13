package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.ump.asyn.callback.RepayNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.TransferNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.ProjectTransferRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class UmpInvestRepayFeeService {

    private static Logger logger = LoggerFactory.getLogger(UmpInvestRepayFeeService.class);

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}X{1}";

    private final UmpUtils umpUtils;

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateMapper updateMapper;

    private final InsertResponseMapper insertResponseMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    @Autowired
    public UmpInvestRepayFeeService(UmpUtils umpUtils, InsertRequestMapper insertRequestMapper, UpdateMapper updateMapper, InsertResponseMapper insertResponseMapper, InsertNotifyMapper insertNotifyMapper){
        this.umpUtils = umpUtils;
        this.insertRequestMapper = insertRequestMapper;
        this.updateMapper = updateMapper;
        this.insertResponseMapper = insertResponseMapper;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage investRepayFee(long loanId, long loanRepayId, long amount, boolean isAdvance){
        ProjectTransferRequestModel model = ProjectTransferRequestModel.newAdvanceRepayInvestFeeRequest(
                String.valueOf(loanId),
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new Date().getTime())),
                String.valueOf(amount));

        if (isAdvance){
            model = ProjectTransferRequestModel.newNormalRepayInvestFeeRequest(String.valueOf(loanId),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new Date().getTime())),
                    String.valueOf(model));
        }

        umpUtils.sign(model);

        insertRequestMapper.insertProjectTransfer(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP register] failed to sign, data: {}", model);
            return new BankBaseMessage(false, "签名失败");
        }

        updateMapper.updateProjectTransfer(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null){
            updateMapper.updateProjectTransfer(SyncRequestStatus.FAILURE, model.getId());
            return new BankBaseMessage(false, "请求联动优势失败");
        }

        updateMapper.updateProjectTransfer(SyncRequestStatus.SUCCESS, model.getId());
        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);
        insertResponseMapper.invertProjectTransfer(responseModel);
        return new BankBaseMessage(true, null);

    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        TransferNotifyRequestModel transferNotifyRequestModel = new TransferNotifyRequestModel();
        umpUtils.parseCallbackRequest(paramsMap, queryString, transferNotifyRequestModel);
        if (Strings.isNullOrEmpty(transferNotifyRequestModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyProjectTransfer(transferNotifyRequestModel);
        return transferNotifyRequestModel.getResponseData();
    }
}
