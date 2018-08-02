package com.tuotiansudai.paywrapper.credit;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanActivateAccountNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanNopwdActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.CreditLoanBillOperationType;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class CreditLoanActivateAccountService {

    private static Logger logger = Logger.getLogger(CreditLoanActivateAccountService.class);

    private final static int ACTIVATE_ACCOUNT_MONEY = 1;

    private final static String ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR = "X";

    private final static String CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY = "credit:loan:activate:account:{0}";

    private final static String CREDIT_LOAN_ACTIVATE_ACCOUNT_CONCURRENCY_REDIS_KEY = "credit:loan:activate:account:concurrency:{0}";

    private final static String ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE = "{0}" + ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR + "{1}";

    private final AccountMapper accountMapper;

    private final UserMapper userMapper;

    private final PaySyncClient paySyncClient;

    private final PayAsyncClient payAsyncClient;

    private final MQWrapperClient mqWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    public CreditLoanActivateAccountService(AccountMapper accountMapper,
                                            UserMapper userMapper,
                                            PaySyncClient paySyncClient,
                                            PayAsyncClient payAsyncClient,
                                            MQWrapperClient mqWrapperClient) {
        this.accountMapper = accountMapper;
        this.userMapper = userMapper;
        this.paySyncClient = paySyncClient;
        this.payAsyncClient = payAsyncClient;
        this.mqWrapperClient = mqWrapperClient;
    }

    public BaseDto<PayFormDataDto> passwordActivateAccount(String mobile) {
        logger.info(MessageFormat.format("[credit loan password activate account {0}] starting", String.valueOf(mobile)));

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setCode(String.valueOf(HttpStatus.OK));
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);

        AccountModel account = this.getAccount(mobile);
        if (account == null) {
            payFormDataDto.setMessage("用户未开通支付账户");
            payFormDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_FAILED));
            return dto;
        }

        if (this.isActive(mobile)) {
            payFormDataDto.setMessage("正在激活账户, 请30分钟后查看");
            payFormDataDto.setCode(String.valueOf(HttpStatus.LOCKED));
            return dto;
        }

        if (!this.checkActivateAccountStatus(mobile)) {
            payFormDataDto.setMessage("您已经激活过账户");
            payFormDataDto.setCode(String.valueOf(HttpStatus.FORBIDDEN));
            return dto;
        }

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanActivateAccountRequest(
                    MessageFormat.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, mobile, String.valueOf(new Date().getTime())),
                    account.getPayUserId(),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY));

            BaseDto<PayFormDataDto> payFormDataDtoBaseDto = payAsyncClient.generateFormData(CreditLoanActivateAccountMapper.class, requestModel);
            redisWrapperClient.setex(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_CONCURRENCY_REDIS_KEY, mobile), 30 * 60, SyncRequestStatus.SENT.name());
            return payFormDataDtoBaseDto;
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan password activate account {0}] activate account error, mobile({1})", mobile), e);
            payFormDataDto.setMessage("发送激活账户数据失败");
            payFormDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return dto;
    }

    public BaseDto<PayDataDto> noPasswordActivateAccount(String mobile) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        AccountModel account = this.getAccount(mobile);
        if (account == null) {
            payDataDto.setMessage("用户未开通支付账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return baseDto;
        }

        if (!account.isNoPasswordInvest()) {
            payDataDto.setMessage("用户未开通免密支付功能");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return baseDto;
        }

        if (!this.checkActivateAccountStatus(mobile)) {
            payDataDto.setMessage("您已经激活过账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return baseDto;
        }

        try {

            ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCreditLoanActivateAccountNopwdRequest(
                    MessageFormat.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, mobile, String.valueOf(new Date().getTime())),
                    account.getPayUserId(),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY));

            redisWrapperClient.set(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY, mobile), SyncRequestStatus.SENT.name());

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    CreditLoanNopwdActivateAccountMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);

            if(!responseModel.isSuccess()){
                redisWrapperClient.del(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY, mobile));
            }
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("callbackUrl", requestModel.getRetUrl())
                    .build()));

            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            logger.error(MessageFormat.format("[慧租无密激活账户] error, mobile:{0}", mobile), e);
            this.sendFatalNotify(MessageFormat.format("慧租无密激活账户异常，mobile:{0}", mobile));
            payDataDto.setMessage("激活账户失败");
            payDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        return baseDto;
    }

    public String activateAccountCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CreditLoanActivateAccountNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.warn("[credit activate account] parse callback error");
            return null;
        }

        String orderId = callbackRequest.getOrderId().split(ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR)[0];

        String key = MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY, orderId);

        mqWrapperClient.sendMessage(MessageQueue.CreditLoanActivateAccountQueue, Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("order_id", orderId)
                .put("success", callbackRequest.isSuccess())
                .build()));

        if (callbackRequest.isSuccess()) {
            if (Strings.isNullOrEmpty(redisWrapperClient.get(key)) || SyncRequestStatus.SENT.name().equalsIgnoreCase(redisWrapperClient.get(key))) {
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                        new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, userMapper.findByMobile(orderId).getLoginName(),
                                Long.parseLong(orderId),
                                ACTIVATE_ACCOUNT_MONEY,
                                UserBillBusinessType.CREDIT_LOAN_ACTIVATE_ACCOUNT));

                mqWrapperClient.sendMessage(MessageQueue.CreditLoanBill,
                        new CreditLoanBillModel(Long.parseLong(orderId), ACTIVATE_ACCOUNT_MONEY, CreditLoanBillOperationType.IN, CreditLoanBillBusinessType.CREDIT_LOAN_ACTIVATE_ACCOUNT, orderId));
            }

            redisWrapperClient.set(key, SyncRequestStatus.SUCCESS.name());
        }

        return callbackRequest.getResponseData();
    }

    private AccountModel getAccount(String mobile) {
        AccountModel accountModel = accountMapper.findByMobile(mobile);
        if (accountModel == null) {
            logger.error(MessageFormat.format("[credit loan no pwd activate account {0}] does not exist ", mobile));
            return null;
        }
        return accountModel;
    }

    private boolean isActive(String mobile) {
        return redisWrapperClient.exists(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_CONCURRENCY_REDIS_KEY, mobile));
    }

    private void sendFatalNotify(String message) {
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, message);
    }

    private boolean checkActivateAccountStatus(String mobile) {
        String status = redisWrapperClient.get(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY, mobile));
        if (Strings.isNullOrEmpty(status) || SyncRequestStatus.valueOf(status) == SyncRequestStatus.FAILURE) {
            return true;
        }
        return false;
    }

}
