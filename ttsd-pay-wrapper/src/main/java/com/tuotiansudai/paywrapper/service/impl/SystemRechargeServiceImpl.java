package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferAsynMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferAsynRequestModel;
import com.tuotiansudai.paywrapper.service.SystemRechargeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.SystemRechargeMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.SystemRechargeModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
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
    private AccountMapper accountMapper;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private SystemRechargeMapper systemRechargeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto dto) {
        UserModel userModel = userMapper.findByMobile(dto.getMobile());
        SystemRechargeModel systemRechargeModel = new SystemRechargeModel(dto, userModel.getLoginName());
        systemRechargeModel.setId(IdGenerator.generate());

        AccountModel accountModel = accountMapper.findByLoginName(systemRechargeModel.getLoginName());

        TransferAsynRequestModel requestModel = TransferAsynRequestModel.createSystemRechargeRequestModel(String.valueOf(systemRechargeModel.getId()),
                accountModel.getPayUserId(), accountModel.getPayAccountId(), String.valueOf(systemRechargeModel.getAmount()));
        String remark = MessageFormat.format("{0} 从 {1} 账户为平台账户充值 {2} 元", dto.getOperatorLoginName(),
                dto.getMobile(), dto.getAmount());
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

    @Transactional
    @Override
    public String systemRechargeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, TransferNotifyMapper.class, TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postSystemRechargeCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    private void postSystemRechargeCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            SystemRechargeModel systemRechargeModel = systemRechargeMapper.findById(orderId);
            if (systemRechargeModel == null) {
                logger.error(MessageFormat.format("system_recharge callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            if (systemRechargeModel.getStatus() != RechargeStatus.WAIT_PAY) {
                logger.warn(MessageFormat.format("System has dealt with the system recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = systemRechargeModel.getLoginName();
            long amount = systemRechargeModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                systemRechargeModel.setSuccessTime(new Date());
                systemRechargeModel.setStatus(RechargeStatus.SUCCESS);
                systemRechargeMapper.updateSystemRecharge(systemRechargeModel);

                AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loginName, orderId, amount, UserBillBusinessType.SYSTEM_RECHARGE);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

                SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_IN,
                        orderId, amount, SystemBillBusinessType.SYSTEM_RECHARGE,
                        MessageFormat.format("{0}充值到平台账户{1}", loginName, amount));
                mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);

            } else {
                systemRechargeModel.setStatus(RechargeStatus.FAIL);
                systemRechargeMapper.updateSystemRecharge(systemRechargeModel);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("System Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
