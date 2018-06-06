package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.HTrackingClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerRechargePersonMapper;
import com.tuotiansudai.paywrapper.repository.mapper.RechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.RechargeNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargePersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargeRequestModel;
import com.tuotiansudai.paywrapper.service.RechargeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class RechargeServiceImpl implements RechargeService {

    static Logger logger = Logger.getLogger(RechargeServiceImpl.class);

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HTrackingClient hTrackingClient;

    private final static String HTRACKING_CHANNEL = "htracking";

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String HUIZU_ACTIVE_RECHARGE_KEY = "huizu_active_recharge:";

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> recharge(RechargeDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        RechargeModel rechargeModel = new RechargeModel(dto);
        rechargeModel.setId(IdGenerator.generate());

        if (dto.isPublicPay()) {
            return this.generatePublicRechargeFormData(rechargeModel);
        }

        return generateRechargeFormData(dto, accountModel, rechargeModel);
    }

    private BaseDto<PayFormDataDto> generateRechargeFormData(RechargeDto dto, AccountModel accountModel, RechargeModel rechargeModel) {
        MerRechargePersonRequestModel requestModel = MerRechargePersonRequestModel.newRecharge(String.valueOf(rechargeModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(rechargeModel.getAmount()),
                rechargeModel.getBankCode());

        if (dto.isFastPay()) {
            requestModel = MerRechargePersonRequestModel.newFastRecharge(String.valueOf(rechargeModel.getId()),
                    accountModel.getPayUserId(),
                    String.valueOf(rechargeModel.getAmount()), dto.getSource());
        }
        try {
            if (dto.isHuizuActive())
                redisWrapperClient.setex(HUIZU_ACTIVE_RECHARGE_KEY + rechargeModel.getId(), 60 * 60 * 24, "1"); // 标记此次充值是慧租激活账户充值

            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(MerRechargePersonMapper.class, requestModel);
            rechargeMapper.create(rechargeModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    private BaseDto<PayFormDataDto> generatePublicRechargeFormData(RechargeModel rechargeModel) {
        MerRechargeRequestModel requestModel = new MerRechargeRequestModel(String.valueOf(rechargeModel.getId()),
                String.valueOf(rechargeModel.getAmount()),
                rechargeModel.getBankCode());

        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(MerRechargeMapper.class, requestModel);
            rechargeMapper.create(rechargeModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    @Override
    @Transactional
    public String rechargeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, RechargeNotifyMapper.class, RechargeNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    private void postCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            RechargeModel rechargeModel = rechargeMapper.findById(orderId);
            if (rechargeModel == null) {
                logger.error(MessageFormat.format("Recharge callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            if (rechargeModel.getStatus() != RechargeStatus.WAIT_PAY) {
                logger.warn(MessageFormat.format("System has dealt with the recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            long amount = rechargeModel.getAmount();
            if (!callbackRequestModel.isSuccess()) {
                rechargeMapper.updateStatus(orderId, RechargeStatus.FAIL);
                return;
            }

            String loginName = rechargeModel.getLoginName();
            rechargeMapper.updateStatus(orderId, RechargeStatus.SUCCESS);

            this.postRechargeCallback(orderId, loginName, amount);

            mqWrapperClient.sendMessage(MessageQueue.RechargeSuccess_CompletePointTask, rechargeModel.getLoginName());

        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void postRechargeCallback(long orderId, String loginName, long amount) {
        AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, loginName, orderId, amount, UserBillBusinessType.RECHARGE_SUCCESS);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
    }
}
