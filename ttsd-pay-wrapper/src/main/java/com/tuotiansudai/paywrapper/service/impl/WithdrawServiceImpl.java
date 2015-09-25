package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CustWithdrawalsMapper;
import com.tuotiansudai.paywrapper.repository.mapper.WithdrawApplyNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.WithdrawNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.WithdrawApplyNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.WithdrawNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.CustWithdrawalsRequestModel;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.paywrapper.service.WithdrawService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class WithdrawServiceImpl implements WithdrawService {

    static Logger logger = Logger.getLogger(WithdrawServiceImpl.class);

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserBillService userBillService;


    @Override
    @Transactional
    public BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto) {
        AccountModel accountModel = accountMapper.findByLoginName(withdrawDto.getLoginName());
        WithdrawModel withdrawModel = new WithdrawModel(withdrawDto);
        withdrawModel.setId(idGenerator.generate());
        CustWithdrawalsRequestModel requestModel = new CustWithdrawalsRequestModel(String.valueOf(withdrawModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(withdrawModel.getAmount()));
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(CustWithdrawalsMapper.class, requestModel);
            withdrawMapper.create(withdrawModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    @Override
    public String withdrawCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest;

        if (this.isApplyNotify(paramsMap)) {
            callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, WithdrawApplyNotifyMapper.class, WithdrawApplyNotifyRequestModel.class);
            this.postWithdrawApplyCallback(callbackRequest);
        } else {
            callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, WithdrawNotifyMapper.class, WithdrawNotifyRequestModel.class);
            this.postWithdrawNotifyCallback(callbackRequest);
        }

        return callbackRequest != null ? callbackRequest.getResponseData() : null;
    }

    @Transactional
    private void postWithdrawApplyCallback(BaseCallbackRequestModel callbackRequestModel) {
        if (callbackRequestModel == null) {
            return;
        }
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            WithdrawModel withdrawModel = withdrawMapper.findById(orderId);
            if (withdrawModel == null) {
                logger.error(MessageFormat.format("Withdraw callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = withdrawModel.getLoginName();
            long amount = withdrawModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                userBillService.freeze(loginName, orderId, amount, UserBillBusinessType.APPLY_WITHDRAW);
                withdrawModel.setStatus(WithdrawStatus.RECHECK);
                //TODO update system bill
            } else {
                withdrawModel.setStatus(WithdrawStatus.VERIFY_FAIL);
            }
            withdrawModel.setVerifyTime(new Date());
            withdrawModel.setVerifyMessage(callbackRequestModel.getRetMsg());
            withdrawMapper.update(withdrawModel);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Withdraw callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        } catch (AmountTransferException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Transactional
    private void postWithdrawNotifyCallback(BaseCallbackRequestModel callbackRequestModel) {
        if (callbackRequestModel == null) {
            return;
        }
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            WithdrawModel withdrawModel = withdrawMapper.findById(orderId);
            if (withdrawModel == null) {
                logger.error(MessageFormat.format("Withdraw callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }
            String loginName = withdrawModel.getLoginName();
            long amount = withdrawModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                userBillService.transferOutFreeze(loginName, orderId, amount, UserBillBusinessType.WITHDRAW_SUCCESS);
                withdrawModel.setStatus(WithdrawStatus.SUCCESS);
                //TODO update system bill
            } else {
                withdrawModel.setStatus(WithdrawStatus.RECHECK_FAIL);
            }
            withdrawModel.setRecheckTime(new Date());
            withdrawModel.setRecheckMessage(callbackRequestModel.getRetMsg());
            withdrawMapper.update(withdrawModel);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Withdraw callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        } catch (AmountTransferException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean isApplyNotify(Map<String, String> paramsMap) {
        return paramsMap.containsKey("service");
    }
}
