package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerRechargePersonMapper;
import com.tuotiansudai.paywrapper.repository.mapper.RechargeNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.RechargeNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargePersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.MerRechargeRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.RechargeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
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
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private PaySyncClient paySyncClient;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> recharge(RechargeDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        RechargeModel rechargeModel = new RechargeModel(dto);
        rechargeModel.setId(idGenerator.generate());

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
        MerRechargeRequestModel requestModel = MerRechargeRequestModel.newRecharge(String.valueOf(rechargeModel.getId()),
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
                logger.error(MessageFormat.format("System has dealt with the recharge (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            long amount = rechargeModel.getAmount();
            if (!callbackRequestModel.isSuccess()) {
                rechargeMapper.updateStatus(orderId, RechargeStatus.FAIL);
                return;
            }

            String loginName = rechargeModel.getLoginName();
            rechargeMapper.updateStatus(orderId, RechargeStatus.SUCCESS);
            if (rechargeModel.isPublicPay()) {
                this.postPublicRechargeCallback(orderId, loginName, amount);
            } else {
                this.postRechargeCallback(orderId, loginName, amount);
            }

        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void postRechargeCallback(long orderId, String loginName, long amount) {
        try {
            amountTransfer.transferInBalance(loginName, orderId, amount, UserBillBusinessType.RECHARGE_SUCCESS, null, null);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("recharge transfer in balance failed (orderId = {0})", String.valueOf(orderId)), e);
        }
    }

    private void postPublicRechargeCallback(long orderId, String loginName, long amount) {
        systemBillService.transferIn(orderId,
                amount,
                SystemBillBusinessType.PUBLIC_RECHARGE_SUCCESS,
                MessageFormat.format(SystemBillDetailTemplate.PUBLIC_RECHARGE_DETAIL_TEMPLATE.getTemplate(), loginName, String.valueOf(amount), String.valueOf(orderId)));
        try {
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            TransferRequestModel requestModel = TransferRequestModel.newRequest(String.valueOf(orderId), accountModel.getPayUserId(), String.valueOf(amount));
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            if (responseModel.isSuccess()) {
                systemBillService.transferOut(orderId,
                        amount,
                        SystemBillBusinessType.PUBLIC_RECHARGE_SUCCESS,
                        MessageFormat.format(SystemBillDetailTemplate.PUBLIC_RECHARGE_DETAIL_TEMPLATE.getTemplate(), loginName, String.valueOf(amount), String.valueOf(orderId)));
                amountTransfer.transferInBalance(loginName, orderId, amount, UserBillBusinessType.RECHARGE_SUCCESS, null, null);
            }
        } catch (PayException | AmountTransferException e) {
            logger.error(MessageFormat.format("public recharge transfer in balance failed (orderId = {0})", String.valueOf(orderId)), e);
        }
    }
}
