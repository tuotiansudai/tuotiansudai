package com.tuotiansudai.paywrapper.credit;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanOutProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanOutProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
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
public class CreditLoanOutService {

    private static Logger logger = Logger.getLogger(CreditLoanOutService.class);

    private final static String CREDIT_LOAN_OUT_REDIS_KEY = "credit:loan:out";

    private final static String CREDIT_LOAN_OUT_INFO_REDIS_KEY = "credit:loan:out:info:{0}";

    private final static String LOAN_OUT_ORDER_ID_SEPARATOR = "X";

    private final static String LOAN_OUT_ORDER_ID_TEMPLATE = "{0}" + LOAN_OUT_ORDER_ID_SEPARATOR + "{1}";

    private final UserMapper userMapper;

    private final AccountMapper accountMapper;

    private final PaySyncClient paySyncClient;

    private final PayAsyncClient payAsyncClient;

    private final MQWrapperClient mqWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    public CreditLoanOutService(UserMapper userMapper,
                                AccountMapper accountMapper,
                                PaySyncClient paySyncClient,
                                PayAsyncClient payAsyncClient,
                                MQWrapperClient mqWrapperClient) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.paySyncClient = paySyncClient;
        this.payAsyncClient = payAsyncClient;
        this.mqWrapperClient = mqWrapperClient;
    }

    public BaseDto<PayDataDto> loanOut(long orderId, String mobile, long amount) {
        logger.info(MessageFormat.format("[credit loan out {0}] loan out starting, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)));
        PayDataDto payDataDto = new PayDataDto(false, "", String.valueOf(HttpStatus.OK));
        BaseDto<PayDataDto> dto = new BaseDto<>(payDataDto);

        if (!this.checkAmount(orderId, amount)) {
            payDataDto.setMessage("贷款金额必须大于零");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        AccountModel account = this.getAccount(orderId, mobile);
        if (account == null) {
            payDataDto.setMessage("用户未开通支付账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if (!this.checkStatus(orderId)) {
            payDataDto.setMessage("放款正在交易中，或已放款，请勿重复放款");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        ProjectTransferRequestModel paybackRequestModel = ProjectTransferRequestModel.newCreditLoanOutRequest(
                MessageFormat.format(LOAN_OUT_ORDER_ID_TEMPLATE, String.valueOf(orderId), String.valueOf(new Date().getTime())),
                account.getPayUserId(),
                String.valueOf(amount));

        try {
            redisWrapperClient.hset(CREDIT_LOAN_OUT_REDIS_KEY, String.valueOf(orderId), SyncRequestStatus.SENT.name());
            redisWrapperClient.set(MessageFormat.format(CREDIT_LOAN_OUT_INFO_REDIS_KEY, String.valueOf(orderId)),
                    MessageFormat.format("{0}|{1}", mobile, String.valueOf(amount)));
            ProjectTransferResponseModel loanOutResponseModel = this.paySyncClient.send(CreditLoanOutProjectTransferMapper.class, paybackRequestModel, ProjectTransferResponseModel.class);
            boolean isSuccess = loanOutResponseModel.isSuccess();
            payDataDto.setStatus(isSuccess);
            if (isSuccess) {
                logger.info(MessageFormat.format("[credit loan out {0}] loan out success, mobile({1}) amount({2})",
                        String.valueOf(orderId), mobile, String.valueOf(amount)));
                payDataDto.setMessage("放款成功");
            } else {
                logger.info(MessageFormat.format("[credit loan out {0}] loan out fail({1}), mobile({2}) amount({3})",
                        String.valueOf(orderId), loanOutResponseModel.getRetMsg(), mobile, String.valueOf(amount)));
                payDataDto.setMessage(loanOutResponseModel.getRetMsg());
                this.sendFatalNotify(MessageFormat.format("慧租信用贷放款失败({0})，订单号({1}), 贷款人({2}), 金额({3})",
                        loanOutResponseModel.getRetMsg(), String.valueOf(orderId), mobile, String.valueOf(amount)));
                payDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
                payDataDto.setMessage(MessageFormat.format("放款失败，{0}", loanOutResponseModel.getRetMsg()));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan out {0}] loan out error, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("慧租信用贷放款异常，订单号({0}), 贷款人({1}), 金额({2})",
                    String.valueOf(orderId), mobile, String.valueOf(amount)));
            payDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            payDataDto.setMessage("慧租信用贷放款异常");
        }

        return dto;
    }

    public String loanOutCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CreditLoanOutProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.warn("[credit loan out] parse callback error");
            return null;
        }

        String orderId = callbackRequest.getOrderId().split(LOAN_OUT_ORDER_ID_SEPARATOR)[0];

        try {
            mqWrapperClient.sendMessage(MessageQueue.CreditLoanOutQueue,
                    Maps.newHashMap(ImmutableMap.<String, Object>builder()
                            .put("order_id", orderId)
                            .put("success", callbackRequest.isSuccess())
                            .build()));

            if (callbackRequest.isSuccess()) {
                logger.info(MessageFormat.format("[credit loan out {0}] loan out callback is success", String.valueOf(orderId)));

                if (SyncRequestStatus.SENT.name().equalsIgnoreCase(redisWrapperClient.hget(CREDIT_LOAN_OUT_REDIS_KEY, orderId))) {
                    String loanOutInfo = redisWrapperClient.get(MessageFormat.format(CREDIT_LOAN_OUT_INFO_REDIS_KEY, String.valueOf(orderId)));
                    String mobile = loanOutInfo.split("\\|")[0];
                    long amount = Long.parseLong(loanOutInfo.split("\\|")[1]);

                    mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                            new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, userMapper.findByMobile(mobile).getLoginName(),
                                    Long.parseLong(orderId),
                                    amount,
                                    UserBillBusinessType.CREDIT_LOAN_OUT));
                    mqWrapperClient.sendMessage(MessageQueue.CreditLoanBill,
                            new CreditLoanBillModel(Long.parseLong(orderId), amount, CreditLoanBillOperationType.OUT, CreditLoanBillBusinessType.CREDIT_LOAN_OFFER, mobile));
                }
            } else {
                logger.error(MessageFormat.format("[credit loan out {0}] loan out callback is failed, error is {1}", String.valueOf(orderId), callbackRequest.getRetMsg()));
            }

            redisWrapperClient.hset(CREDIT_LOAN_OUT_REDIS_KEY, orderId,
                    callbackRequest.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan out {0}] loan out callback is error", String.valueOf(orderId)), e);
        }

        return callbackRequest.getResponseData();
    }

    private boolean checkAmount(long orderId, long amount) {
        if (amount < 1) {
            logger.error(MessageFormat.format("[credit loan out {0}] amount({1}) less than 0", String.valueOf(orderId), String.valueOf(amount)));
            return false;
        }

        return true;
    }

    private boolean checkStatus(long orderId) {
        try {
            String status = redisWrapperClient.hget(CREDIT_LOAN_OUT_REDIS_KEY, String.valueOf(orderId));
            if (Strings.isNullOrEmpty(status) || SyncRequestStatus.valueOf(status) == SyncRequestStatus.FAILURE) {
                return true;
            }
            logger.error(MessageFormat.format("[credit loan out {0}] status is {1}, do not try again", String.valueOf(orderId), status));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan out {0}] status check error", String.valueOf(orderId)), e);
        }

        return false;
    }

    private AccountModel getAccount(long orderId, String mobile) {
        AccountModel accountModel = accountMapper.findByMobile(mobile);
        if (accountModel == null) {
            logger.error(MessageFormat.format("[credit loan out {0}] account({1}) does not exist ", String.valueOf(orderId), mobile));
            return null;
        }

        return accountModel;
    }

    private void sendFatalNotify(String message) {
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, message);
    }
}
