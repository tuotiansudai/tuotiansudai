package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CustWithdrawalsMapper;
import com.tuotiansudai.paywrapper.repository.mapper.RechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferAsynMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.RechargeNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferAsynRequestModel;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.paywrapper.service.SystemRechargeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.SystemRechargeMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.UserBillService;
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
public class SystemRechargeServiceImpl implements SystemRechargeService {

    static Logger logger = Logger.getLogger(SystemRechargeServiceImpl.class);
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private SystemRechargeMapper systemRechargeMapper;
    @Autowired
    private AmountTransfer amountTransfer;
    @Autowired
    private SystemBillService systemBillService;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto dto) {

        SystemRechargeModel systemRechargeModel = new SystemRechargeModel(dto);
        systemRechargeModel.setId(idGenerator.generate());

        AccountModel accountModel = accountMapper.findByLoginName(systemRechargeModel.getLoginName());
        if (accountModel == null){
            logger.debug(systemRechargeModel.getLoginName() + " not certification");
        }
        TransferAsynRequestModel requestModel = new TransferAsynRequestModel(String.valueOf(systemRechargeModel.getAmount()),
                accountModel.getPayUserId(),dto.getAmount());
        String remark = MessageFormat.format("{0} 从 {1} 账户为平台账户充值 {2} 元",dto.getOperatorLoginName(),
                dto.getLoginName(),dto.getAmount());
        systemRechargeModel.setRemark(remark);

        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(TransferAsynMapper.class, requestModel);
            systemRechargeMapper.create(systemRechargeModel);
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
    public String systemRechargeCallback(Map<String, String> paramsMap, String originalQueryString) {
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
                logger.error(MessageFormat.format("system_recharge callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            if (systemRechargeModel.getStatus() != SystemRechargeStatus.WAIT_PAY) {
                logger.error(MessageFormat.format("System has dealt with the system recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = systemRechargeModel.getLoginName();
            long amount = systemRechargeModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                systemRechargeModel.setSuccessTime(new Date());
                systemRechargeModel.setStatus(SystemRechargeStatus.SUCCESS);
                systemRechargeMapper.updateSystemRecharge(systemRechargeModel);
                try {
                    amountTransfer.transferOutBalance(loginName, orderId, amount, UserBillBusinessType.ADMIN_INTERVENTION, null, null);
                    systemBillService.transferIn(orderId, amount, SystemBillBusinessType.ADMIN_INTERVENTION,
                            MessageFormat.format("{0}充值到平台账户{1}",loginName,amount));
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("system recharge transfer out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
                }

            } else {
                systemRechargeModel.setStatus(SystemRechargeStatus.FAIL);
                systemRechargeMapper.updateSystemRecharge(systemRechargeModel);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("System Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }

    }
}
