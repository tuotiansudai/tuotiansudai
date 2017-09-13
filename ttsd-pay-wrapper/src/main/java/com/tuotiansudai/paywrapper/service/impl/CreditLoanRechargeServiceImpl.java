package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRechargeNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.CreditLoanBillService;
import com.tuotiansudai.paywrapper.service.CreditLoanRechargeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.SystemRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SystemRechargeMapper systemRechargeMapper;
    @Autowired
    private AmountTransfer amountTransfer;
    @Autowired
    private CreditLoanBillService creditLoanBillService;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> creditLoanRecharge(SystemRechargeDto dto) {

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        UserModel userModel = userMapper.findByMobile(dto.getMobile());
        SystemRechargeModel systemRechargeModel = new SystemRechargeModel(dto, userModel.getLoginName());
        systemRechargeModel.setId(IdGenerator.generate());

        AccountModel accountModel = accountMapper.findByLoginName(systemRechargeModel.getLoginName());

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newPurchaseNopwdRequest(null,
                String.valueOf(systemRechargeModel.getId()),
                accountModel.getPayUserId(),
                dto.getAmount());

        String remark = MessageFormat.format("{0} 从 {1} 账户为信用贷标的账户充值 {2} 元", dto.getOperatorLoginName(),
                dto.getMobile(), dto.getAmount());
        systemRechargeModel.setRemark(remark);

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    CreditLoanRechargeNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
//            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(TransferAsynMapper.class, requestModel);
            systemRechargeMapper.create(systemRechargeModel);


            return baseDto;
        } catch (PayException e) {
            payFormDataDto.setStatus(false);
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    @Override
    public String creditLoanRechargeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, TransferNotifyMapper.class, TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postSystemRechargeCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postSystemRechargeCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            SystemRechargeModel systemRechargeModel = systemRechargeMapper.findById(orderId);
            if (systemRechargeModel == null) {
                logger.error(MessageFormat.format("credit_loan_recharge callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            if (systemRechargeModel.getStatus() != RechargeStatus.WAIT_PAY) {
                logger.error(MessageFormat.format("credit loan has dealt with the credit loan recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = systemRechargeModel.getLoginName();
            long amount = systemRechargeModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                systemRechargeModel.setSuccessTime(new Date());
                systemRechargeModel.setStatus(RechargeStatus.SUCCESS);
                systemRechargeMapper.updateSystemRecharge(systemRechargeModel);
                try {
                    amountTransfer.transferOutBalance(loginName, orderId, amount, UserBillBusinessType.CREDIT_LOAN_RECHARGE, null, null);
                    creditLoanBillService.transferIn(orderId, amount, SystemBillBusinessType.CREDIT_LOAN_RECHARGE,
                            MessageFormat.format("{0}充值到信用贷标的账户{1}", loginName, amount));
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("credit loan recharge transfer out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
                }

            } else {
                systemRechargeModel.setStatus(RechargeStatus.FAIL);
                systemRechargeMapper.updateSystemRecharge(systemRechargeModel);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("credit loan Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
