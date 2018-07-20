package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertResponseMapper;
import com.tuotiansudai.fudian.mapper.ump.UpdateRequestMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.ump.asyn.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.ProjectTransferRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.SyncRequestStatus;
import com.tuotiansudai.fudian.ump.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.fudian.umpdto.UmpLoanRepayFeeDto;
import com.tuotiansudai.fudian.util.UmpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class UmpLoanRepayFeeService {

    private static Logger logger = LoggerFactory.getLogger(UmpLoanRepayFeeService.class);

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}X{1}";

    private final UmpUtils umpUtils;

    private final InsertRequestMapper insertRequestMapper;

    private final UpdateRequestMapper updateRequestMapper;

    private final InsertResponseMapper insertResponseMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    @Autowired
    public UmpLoanRepayFeeService(UmpUtils umpUtils, InsertRequestMapper insertRequestMapper, UpdateRequestMapper updateRequestMapper, InsertResponseMapper insertResponseMapper, InsertNotifyMapper insertNotifyMapper){
        this.umpUtils = umpUtils;
        this.insertRequestMapper = insertRequestMapper;
        this.updateRequestMapper = updateRequestMapper;
        this.insertResponseMapper = insertResponseMapper;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage loanRepayFee(UmpLoanRepayFeeDto dto){
        ProjectTransferRequestModel model = ProjectTransferRequestModel.newNormalRepayInvestFeeRequest(
                String.valueOf(dto.getLoanId()),
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(dto.getLoanRepayId()), String.valueOf(new Date().getTime())),
                String.valueOf(dto.getAmount()));

        if (dto.isAdvance()){
            model = ProjectTransferRequestModel.newAdvanceRepayInvestFeeRequest(String.valueOf(dto.getLoanId()),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(dto.getLoanRepayId()), String.valueOf(new Date().getTime())),
                    String.valueOf(model));
        }

        umpUtils.sign(model);

        insertRequestMapper.insertProjectTransfer(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP register] failed to sign, data: {}", model);
            return new BankBaseMessage(false, "签名失败");
        }

        updateRequestMapper.updateProjectTransfer(SyncRequestStatus.SENT, model.getId());
        String responseBody = umpUtils.send(model.getRequestUrl(), (Map<String, String>) model.getField());
        if (responseBody == null){
            updateRequestMapper.updateProjectTransfer(SyncRequestStatus.FAILURE, model.getId());
            return new BankBaseMessage(false, "请求联动优势失败");
        }

        updateRequestMapper.updateProjectTransfer(SyncRequestStatus.SUCCESS, model.getId());
        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        umpUtils.generateResponse(model.getId(), responseBody, responseModel);
        insertResponseMapper.invertProjectTransfer(responseModel);
        return new BankBaseMessage(true, null);

    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        ProjectTransferNotifyRequestModel transferNotifyRequestModel = umpUtils.parseCallbackRequest(paramsMap, queryString, ProjectTransferNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(transferNotifyRequestModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyProjectTransfer(transferNotifyRequestModel);
        return transferNotifyRequestModel.getResponseData();
    }
}
