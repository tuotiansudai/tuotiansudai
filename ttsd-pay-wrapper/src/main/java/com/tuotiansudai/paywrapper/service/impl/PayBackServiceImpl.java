package com.tuotiansudai.paywrapper.service.impl;


import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.service.PayBackService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.utils.AmountTransfer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class PayBackServiceImpl implements PayBackService{

    static Logger logger = Logger.getLogger(PayBackServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Override
    public BaseDto<PayFormDataDto> payBack(InvestDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        InvestModel investModel = new InvestModel(dto);
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newPayBackRequest(dto.getLoanId(),String.valueOf(investModel.getId())+"P"+System.currentTimeMillis(),
                accountModel.getPayUserId(),String.valueOf(investModel.getAmount()));
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    @Override
    public String payBackCallback(Map<String, String> paramsMap, String queryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        String orderIdOri = callbackRequest.getOrderId();
        String orderIdStr = orderIdOri == null ? "" : orderIdOri.split("P")[0];
        long orderId = Long.parseLong(orderIdStr);
        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest callback notify order is not exist (orderId = {0})", orderId));
            return null;
        }
        String loginName = investModel.getLoginName();
        if (callbackRequest.isSuccess()) {
            investMapper.updateStatus(investModel.getId(), InvestStatus.CANCEL_INVEST_PAYBACK);
            try {
                amountTransfer.unfreeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.CANCEL_INVEST_PAYBACK, null, null);
            } catch (AmountTransferException e) {
                logger.error(e.getLocalizedMessage(),e);
            }
        } else {
            //TODO SEND_SMS
//            smsWrapperClient.
        }
        String respData = callbackRequest.getResponseData();
        return respData;
    }

}
