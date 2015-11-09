package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerRechargePersonMapper;
import com.tuotiansudai.paywrapper.repository.mapper.RechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.RechargeNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargePersonRequestModel;
import com.tuotiansudai.paywrapper.service.RechargeService;
import com.tuotiansudai.service.AmountTransferService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.utils.IdGenerator;
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
    private IdGenerator idGenerator;

    @Autowired
    private AmountTransferService amountTransferService;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> recharge(RechargeDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        RechargeModel rechargeModel = new RechargeModel(dto);
        rechargeModel.setId(idGenerator.generate());

        MerRechargePersonRequestModel requestModel = MerRechargePersonRequestModel.newRecharge(String.valueOf(rechargeModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(rechargeModel.getAmount()),
                rechargeModel.getBankCode());

        if (dto.isFastPay()) {
            requestModel = MerRechargePersonRequestModel.newFastRecharge(String.valueOf(rechargeModel.getId()),
                    accountModel.getPayUserId(),
                    String.valueOf(rechargeModel.getAmount()));
        }

        try {
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

    @Override
    public String rechargeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, RechargeNotifyMapper.class, RechargeNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postRechargeCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postRechargeCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            RechargeModel rechargeModel = rechargeMapper.findById(orderId);
            if (rechargeModel == null) {
                logger.error(MessageFormat.format("Recharge callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            if (rechargeModel.getStatus() != RechargeStatus.WAIT_PAY) {
                logger.error(MessageFormat.format("System has dealt with the recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = rechargeModel.getLoginName();
            long amount = rechargeModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                rechargeMapper.updateStatus(orderId, RechargeStatus.SUCCESS);
                try {
                    amountTransferService.transferInBalance(loginName, orderId, amount, UserBillBusinessType.RECHARGE_SUCCESS, null, null);
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("recharge transfer in balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
                }
                //TODO update system bill
            } else {
                rechargeMapper.updateStatus(orderId, RechargeStatus.FAIL);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }

    }
}
