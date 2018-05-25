package com.tuotiansudai.paywrapper.credit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanNopwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanPwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class CreditLoanRechargeService{

    static Logger logger = Logger.getLogger(CreditLoanRechargeService.class);
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private MQWrapperClient mqWrapperClient;
    @Autowired
    private CreditLoanRechargeMapper creditLoanRechargeMapper;

    @Value(value = "${credit.loan.agent}")
    private String creditLoanAgent;

    @Transactional
    public BaseDto<PayDataDto> creditLoanRechargeNoPwd(CreditLoanRechargeDto creditLoanRechargeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        if (!creditLoanAgent.equals(creditLoanRechargeDto.getMobile())) {
            payDataDto.setMessage("该资金来源账户不是信用贷代理人");
            return baseDto;
        }

        AccountModel accountModel = accountMapper.findByMobile(creditLoanRechargeDto.getMobile());

        CreditLoanRechargeModel model = new CreditLoanRechargeModel(creditLoanRechargeDto, accountModel.getLoginName());
        model.setId(IdGenerator.generate());

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCreditLoanRechargeNopwdRequest(
                String.valueOf(model.getId()),
                accountModel.getPayUserId(),
                String.valueOf(model.getAmount()));
        try {
            creditLoanRechargeMapper.create(model);
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    CreditLoanNopwdRechargeMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", String.valueOf(model.getId()))
                    .build()));
            return baseDto;
        } catch (PayException e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(MessageFormat.format("{0} user {1} account recharge credit loan  is failed", creditLoanRechargeDto.getOperatorLoginName(), accountModel.getLoginName()), e);
        }
        return baseDto;
    }

    @Transactional
    public BaseDto<PayFormDataDto> creditLoanRecharge(CreditLoanRechargeDto creditLoanRechargeDto) {
        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        dto.setData(payFormDataDto);

        if (!creditLoanAgent.equals(creditLoanRechargeDto.getMobile())) {
            payFormDataDto.setMessage("该资金来源账户不是信用贷代理人");
            return dto;
        }

        AccountModel accountModel = accountMapper.findByMobile(creditLoanRechargeDto.getMobile());

        CreditLoanRechargeModel creditLoanRechargeModel = new CreditLoanRechargeModel(creditLoanRechargeDto, accountModel.getLoginName());
        creditLoanRechargeModel.setId(IdGenerator.generate());

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanRechargePwdRequest(
                String.valueOf(creditLoanRechargeModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(creditLoanRechargeModel.getAmount()));

        try {
            creditLoanRechargeMapper.create(creditLoanRechargeModel);
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(CreditLoanPwdRechargeMapper.class, requestModel);
            return baseDto;
        } catch (PayException e) {
            logger.error(MessageFormat.format("{0} user {1} account recharge credit loan  is failed", creditLoanRechargeDto.getOperatorLoginName(), accountModel.getLoginName()), e);
        }
        return dto;
    }

    @Transactional
    public String creditLoanRechargeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, CreditLoanRechargeNotifyMapper.class,
                TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postCreditLoanRechargeCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    private void postCreditLoanRechargeCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            CreditLoanRechargeModel creditLoanRechargeModel = creditLoanRechargeMapper.findById(orderId);
            if (creditLoanRechargeModel == null) {
                logger.error(MessageFormat.format("credit_loan_recharge callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            if (creditLoanRechargeModel.getStatus() != RechargeStatus.WAIT_PAY) {
                logger.error(MessageFormat.format("credit loan has dealt with the credit loan recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = creditLoanRechargeModel.getAccountName();
            long amount = creditLoanRechargeModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                creditLoanRechargeMapper.updateCreditLoanRechargeStatus(creditLoanRechargeModel.getId(), RechargeStatus.SUCCESS);
                AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loginName, orderId, amount, UserBillBusinessType.CREDIT_LOAN_RECHARGE);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                mqWrapperClient.sendMessage(MessageQueue.CreditLoanBill, new CreditLoanBillModel(orderId, amount, CreditLoanBillOperationType.IN, CreditLoanBillBusinessType.CREDIT_LOAN_RECHARGE, creditLoanAgent));
            } else {
                creditLoanRechargeMapper.updateCreditLoanRechargeStatus(creditLoanRechargeModel.getId(), RechargeStatus.FAIL);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("credit loan Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
