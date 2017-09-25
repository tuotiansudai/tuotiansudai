package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanBillService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanNopwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanPwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.CreditLoanRechargeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class CreditLoanRechargeServiceImpl implements CreditLoanRechargeService {

    static Logger logger = Logger.getLogger(CreditLoanRechargeServiceImpl.class);
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private AmountTransfer amountTransfer;
    @Autowired
    private CreditLoanBillService creditLoanBillService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CreditLoanRechargeMapper creditLoanRechargeMapper;

    @Value(value = "${credit.loan.id}")
    private String creditLoanId;

    @Value(value = "${credit.loan.agent}")
    private String creditLoanAgent;

    @Override
    @Transactional
    public BaseDto<PayDataDto> creditLoanRechargeNoPwd(CreditLoanRechargeDto creditLoanRechargeDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        if (!creditLoanAgent.equals(creditLoanRechargeDto.getMobile())){
            payDataDto.setMessage("该资金来源账户不是信用贷代理人");
            return baseDto;
        }

        UserModel userModel = userMapper.findByMobile(creditLoanRechargeDto.getMobile());

        CreditLoanRechargeModel model = new CreditLoanRechargeModel(creditLoanRechargeDto, userModel.getLoginName());
        model.setId(IdGenerator.generate());

        AccountModel accountModel = accountMapper.findByLoginName(model.getAccountName());

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCreditLoanRechargeNopwdRequest(creditLoanId,
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
            logger.error(MessageFormat.format("{0} user {1} account recharge credit loan  is failed", creditLoanRechargeDto.getOperatorLoginName(), userModel.getLoginName()), e);
        }
        return baseDto;
    }

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> creditLoanRecharge(CreditLoanRechargeDto creditLoanRechargeDto) {
        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        dto.setData(payFormDataDto);

        if (!creditLoanAgent.equals(creditLoanRechargeDto.getMobile())){
            payFormDataDto.setMessage("该资金来源账户不是信用贷代理人");
            return dto;
        }

        UserModel userModel = userMapper.findByMobile(creditLoanRechargeDto.getMobile());

        CreditLoanRechargeModel creditLoanRechargeModel = new CreditLoanRechargeModel(creditLoanRechargeDto, userModel.getLoginName());
        creditLoanRechargeModel.setId(IdGenerator.generate());

        AccountModel accountModel = accountMapper.findByLoginName(creditLoanRechargeModel.getAccountName());

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanRequest(
                creditLoanId,
                String.valueOf(creditLoanRechargeModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(creditLoanRechargeModel.getAmount()));

        try {
            creditLoanRechargeMapper.create(creditLoanRechargeModel);
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(CreditLoanPwdRechargeMapper.class, requestModel);
            return baseDto;
        } catch (PayException e) {
            logger.error(MessageFormat.format("{0} user {1} account recharge credit loan  is failed", creditLoanRechargeDto.getOperatorLoginName(), userModel.getLoginName()), e);
        }
        return dto;
    }

    @Override
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
                try {
                    amountTransfer.transferOutBalance(loginName, orderId, amount, UserBillBusinessType.CREDIT_LOAN_RECHARGE, null, null);
                    creditLoanBillService.transferIn(orderId, amount, CreditLoanBillBusinessType.CREDIT_LOAN_RECHARGE, loginName);
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("credit loan recharge transfer out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
                }

            } else {
                creditLoanRechargeMapper.updateCreditLoanRechargeStatus(creditLoanRechargeModel.getId(), RechargeStatus.FAIL);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("credit loan Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
