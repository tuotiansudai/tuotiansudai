package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.BankCardApplyNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.BankCardNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerBindCardMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerReplaceCardMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BankCardApplyNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BankCardNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.PtpMerBindCardRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.PtpMerReplaceCardRequestModel;
import com.tuotiansudai.paywrapper.service.BindBankCardService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    static Logger logger = Logger.getLogger(BindBankCardServiceImpl.class);

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        UserModel userModel = userMapper.findByLoginName(dto.getLoginName());
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        BankCardModel bankCardModel = new BankCardModel(dto);
        bankCardModel.setId(IdGenerator.generate());
        bankCardModel.setStatus(BankCardStatus.UNCHECKED);

        PtpMerBindCardRequestModel requestModel = new PtpMerBindCardRequestModel(String.valueOf(bankCardModel.getId()),
                dto.getCardNumber(),
                accountModel.getPayUserId(),
                userModel.getUserName(),
                userModel.getIdentityNumber(),
                dto.getSource(),
                dto.isFastPay());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(PtpMerBindCardMapper.class, requestModel);
            bankCardMapper.create(bankCardModel);
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
    public BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto) {
        UserModel userModel = userMapper.findByLoginName(dto.getLoginName());
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        BankCardModel bankCardModel = new BankCardModel(dto);
        bankCardModel.setId(IdGenerator.generate());
        bankCardModel.setStatus(BankCardStatus.UNCHECKED);
        bankCardModel.setIsFastPayOn(bankCardMapper.findByLoginNameAndIsFastPayOn(dto.getLoginName()) != null);
        PtpMerReplaceCardRequestModel requestModel = new PtpMerReplaceCardRequestModel(String.valueOf(bankCardModel.getId()),
                dto.getCardNumber(),
                accountModel.getPayUserId(),
                userModel.getUserName(),
                userModel.getIdentityNumber(), dto.getSource());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(PtpMerReplaceCardMapper.class, requestModel);
            bankCardMapper.create(bankCardModel);
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
    public String replaceBankCardCallback(Map<String, String> paramsMap, String originalQueryString) {

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, BankCardNotifyMapper.class, BankCardNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            this.postReplaceBankCardCallback((BankCardNotifyRequestModel) callbackRequest);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional
    public String bindBankCardCallback(Map<String, String> paramsMap, String originalQueryString) {

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, BankCardNotifyMapper.class, BankCardNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            this.postBindBankCardCallback((BankCardNotifyRequestModel) callbackRequest);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    public String bindBankCardApplyCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, BankCardApplyNotifyMapper.class, BankCardApplyNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        long orderId = Long.parseLong(callbackRequest.getOrderId());
        BankCardModel bankCardModel = bankCardMapper.findById(orderId);
        if (bankCardModel == null) {
            logger.warn(MessageFormat.format("replace bank card order id {0} is not found", String.valueOf(orderId)));
            return null;
        }
        if (callbackRequest.isSuccess()) {
            if (bankCardModel.getStatus() != BankCardStatus.PASSED) {
                bankCardModel.setStatus(BankCardStatus.APPLY);
                bankCardMapper.update(bankCardModel);

            }
        }
        return callbackRequest.getResponseData();
    }

    private void postReplaceBankCardCallback(BankCardNotifyRequestModel callbackRequestModel) throws AmountTransferException {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            BankCardModel bankCardModel = bankCardMapper.findById(orderId);
            if (bankCardModel == null) {
                logger.warn(MessageFormat.format("replace bank card order id {0} is not found", String.valueOf(orderId)));
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                bankCardModel.setStatus(BankCardStatus.PASSED);
                String bankCode = callbackRequestModel.getGateId();
                bankCardModel.setBankCode(bankCode);

                BankCardModel previousBankCard = bankCardMapper.findPassedBankCardByLoginName(bankCardModel.getLoginName());
                previousBankCard.setStatus(BankCardStatus.REMOVED);
                bankCardMapper.update(previousBankCard);
            } else {
                bankCardModel.setStatus(BankCardStatus.FAILED);
            }
            bankCardMapper.update(bankCardModel);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("replace bank card notify request order {0} is not a number", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private void postBindBankCardCallback(BankCardNotifyRequestModel callbackRequestModel) {
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            BankCardModel bankCardModel = bankCardMapper.findById(orderId);
            if (bankCardModel == null) {
                logger.warn(MessageFormat.format("bind bank card order id {0} is not found", String.valueOf(orderId)));
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                bankCardModel.setStatus(BankCardStatus.PASSED);
                String bankCode = callbackRequestModel.getGateId();
                bankCardModel.setBankCode(bankCode);
                bankCardModel.setIsFastPayOn(callbackRequestModel.isOpenPay());
                mqWrapperClient.sendMessage(MessageQueue.BindBankCard_CompletePointTask, bankCardModel.getLoginName());
            } else {
                bankCardModel.setStatus(BankCardStatus.FAILED);
            }
            bankCardMapper.update(bankCardModel);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("bind bank card notify request order {0} is not a number", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
