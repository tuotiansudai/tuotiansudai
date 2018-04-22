package com.tuotiansudai.paywrapper.luxury;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayNoPwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferNotifyMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class LuxuryStageRepayService {

    private static Logger logger = Logger.getLogger(LuxuryStageRepayService.class);

    private final static String LUXURY_STAGE_REPAY_REDIS_KEY = "luxury:stage:repay:{0}:{1}";

    private final static String LUXURY_STAGE_REPAY_INFO_REDIS_KEY = "luxury:stage:repay:info:{0}";

    private final static String LUXURY_STAGE_REPAY_EXPIRED_REDIS_KEY = "luxury:stage:repay:expired:{0}:{1}";

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final UserMapper userMapper;

    private final AccountMapper accountMapper;

    private final PayAsyncClient payAsyncClient;

    private final PaySyncClient paySyncClient;

    private final MQWrapperClient mqWrapperClient;

    private final SmsWrapperClient smsWrapperClient;

    @Autowired
    public LuxuryStageRepayService(UserMapper userMapper, AccountMapper accountMapper, PayAsyncClient payAsyncClient, PaySyncClient paySyncClient, MQWrapperClient mqWrapperClient, SmsWrapperClient smsWrapperClient) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.payAsyncClient = payAsyncClient;
        this.paySyncClient = paySyncClient;
        this.mqWrapperClient = mqWrapperClient;
        this.smsWrapperClient = smsWrapperClient;
    }

    @Transactional
    public BaseDto<PayFormDataDto> repay(long luxuryOrderId, int period, String mobile, long amount) {
        logger.info(MessageFormat.format("[luxury stage repay {0}] repay starting, period({1}) mobile({2}) amount({3})",
                String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount)));

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setCode("0000");
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);

        if (amount < 1) {
            payFormDataDto.setMessage("还款金额必须大于零");
            payFormDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        AccountModel account = accountMapper.findByMobile(mobile);
        if (account == null) {
            payFormDataDto.setMessage("用户未开通支付账户");
            payFormDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        if (this.isRepaying(luxuryOrderId, period)) {
            payFormDataDto.setMessage("还款交易进行中, 请30分钟后查看");
            payFormDataDto.setCode(String.valueOf(HttpStatus.LOCKED));
            return dto;
        }

        if (this.isFinished(luxuryOrderId, period)) {
            payFormDataDto.setMessage("还款已完成, 请勿重复还款");
            payFormDataDto.setCode(String.valueOf(HttpStatus.FORBIDDEN));
            return dto;
        }

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLuxuryStageRepayRequest(
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(luxuryOrderId), String.valueOf(new Date().getTime())),
                    String.valueOf(luxuryOrderId),
                    String.valueOf(period),
                    account.getPayUserId(),
                    String.valueOf(amount));
            BaseDto<PayFormDataDto> payFormDataDtoBaseDto = payAsyncClient.generateFormData(LuxuryStageRepayProjectTransferMapper.class, requestModel);
            redisWrapperClient.setex(MessageFormat.format(LUXURY_STAGE_REPAY_EXPIRED_REDIS_KEY, String.valueOf(luxuryOrderId), String.valueOf(period)), 30 * 60, SyncRequestStatus.SENT.name());
            redisWrapperClient.setex(MessageFormat.format(LUXURY_STAGE_REPAY_INFO_REDIS_KEY, String.valueOf(luxuryOrderId)),
                    24 * 60 * 60,
                    MessageFormat.format("{0}|{1}|{2}|{3}", mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount)));
            redisWrapperClient.set(MessageFormat.format(LUXURY_STAGE_REPAY_REDIS_KEY, String.valueOf(luxuryOrderId), String.valueOf(period)),
                    SyncRequestStatus.SENT.name());
            logger.info(MessageFormat.format("[luxury stage repay {0}] generate form success, period({1}) mobile({2}) amount({3})",
                    String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount)));

            return payFormDataDtoBaseDto;
        } catch (Exception e) {
            logger.error(MessageFormat.format("[luxury stage repay {0}] generate form fail, period({1}) mobile({2}) amount({3})",
                    String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount)), e);
            payFormDataDto.setMessage("生成交易数据失败");
            payFormDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return dto;
    }

    @Transactional
    public BaseDto<PayDataDto> noPasswordRepay(long luxuryOrderId, int period, String mobile, long amount, boolean autoRepay) {
        logger.info(MessageFormat.format("[luxury stage no passward repay {0}] repay starting, period({1}) mobile({2}) amount({3}) autoRepay({4})",
                String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount), autoRepay));

        PayDataDto payDataDto = new PayDataDto(false, "", "0000");
        BaseDto<PayDataDto> dto = new BaseDto<>(payDataDto);

        if (amount < 1) {
            payDataDto.setMessage("还款金额必须大于零");
            payDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        AccountModel account = accountMapper.findByMobile(mobile);
        if (account == null) {
            payDataDto.setMessage("用户未开通支付账户");
            payDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        if (autoRepay && !account.isAutoInvest()) {
            payDataDto.setMessage("用户未签署免密协议");
            payDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        if (!autoRepay && !account.isNoPasswordInvest()) {
            payDataDto.setMessage("用户未开通免密支付功能");
            payDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        if (this.isRepaying(luxuryOrderId, period)) {
            payDataDto.setMessage("还款交易进行中, 请30分钟后查看");
            payDataDto.setCode(String.valueOf(HttpStatus.LOCKED));
            return dto;
        }

        if (this.isFinished(luxuryOrderId, period)) {
            payDataDto.setMessage("还款已完成, 请勿重复还款");
            payDataDto.setCode(String.valueOf(HttpStatus.FORBIDDEN));
            return dto;
        }

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newLuxuryStageRepayNopwdRequest(
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(luxuryOrderId), String.valueOf(new Date().getTime())),
                account.getPayUserId(),
                String.valueOf(amount));

        try {
            redisWrapperClient.setex(MessageFormat.format(LUXURY_STAGE_REPAY_INFO_REDIS_KEY, String.valueOf(luxuryOrderId)),
                    24 * 60 * 60,
                    MessageFormat.format("{0}|{1}|{2}|{3}", mobile, String.valueOf(luxuryOrderId), String.valueOf(period), String.valueOf(amount)));
            redisWrapperClient.set(MessageFormat.format(LUXURY_STAGE_REPAY_REDIS_KEY, String.valueOf(luxuryOrderId), String.valueOf(period)),
                    SyncRequestStatus.SENT.name());

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(LuxuryStageRepayNoPwdMapper.class, requestModel, ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("callbackUrl", requestModel.getRetUrl())
                    .build()));

            if (responseModel.isSuccess()) {
                logger.info(MessageFormat.format("[luxury no password repay {0}] call umpay success, period({1}) mobile({2}) amount({3})", String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount)));
            } else {
                logger.error(MessageFormat.format("[luxury no password repay {0} is auto:{1}] call umpay fail , period({2}) mobile{3} amount({4}), code({5}) message({6})",
                        String.valueOf(luxuryOrderId), autoRepay, String.valueOf(period), mobile, String.valueOf(amount), responseModel.getRetCode(), responseModel.getRetMsg()));
            }

        } catch (Exception e) {
            logger.info(MessageFormat.format("[luxury no password repay {0}] call umpay exception, period({1}) mobile({2}) amount({3})", String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount)), e);
            this.sendFatalNotify(MessageFormat.format("慧租奢侈品无密还款异常，luxuryOrderId:({0}), period({1}) mobile({2}) amount({3})",
                    String.valueOf(luxuryOrderId), String.valueOf(period), mobile, String.valueOf(amount)));
            payDataDto.setMessage("还款交易失败");
            payDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return dto;
    }

    public String repayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                LuxuryStageRepayProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.warn("[luxury stage repay] parse callback error");
            return null;
        }

        String orderId = callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0];

        String luxuryRepayInfo = redisWrapperClient.get(MessageFormat.format(LUXURY_STAGE_REPAY_INFO_REDIS_KEY, orderId));
        String mobile = luxuryRepayInfo.split("\\|")[0];
        int period = Integer.parseInt(luxuryRepayInfo.split("\\|")[2]);
        long amount = Long.parseLong(luxuryRepayInfo.split("\\|")[3]);


        String key = MessageFormat.format(LUXURY_STAGE_REPAY_REDIS_KEY, orderId, String.valueOf(period));

        mqWrapperClient.sendMessage(MessageQueue.LuxuryStageRepayQueue, Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("order_id", Long.parseLong(orderId))
                .put("period", period)
                .put("success", callbackRequest.isSuccess())
                .build()));

        if (callbackRequest.isSuccess()) {
            if (SyncRequestStatus.SENT.name().equalsIgnoreCase(redisWrapperClient.get(key))) {
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                        new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, userMapper.findByMobile(mobile).getLoginName(),
                                Long.parseLong(orderId),
                                amount,
                                period == 0 ? UserBillBusinessType.LUXURY_STAGE_PURCHASE : UserBillBusinessType.LUXURY_STAGE_REPAY,
                                null,
                                null));

                mqWrapperClient.sendMessage(MessageQueue.CreditLoanBill,
                        new CreditLoanBillModel(Long.parseLong(orderId) * 100 + period,
                                amount,
                                CreditLoanBillOperationType.IN,
                                CreditLoanBillBusinessType.LUXURY_STAGE_REPAY,
                                mobile));
            }

            redisWrapperClient.set(key, SyncRequestStatus.SUCCESS.name());
        }

        return callbackRequest.getResponseData();
    }

    private boolean isRepaying(long luxuryOrderId, int period) {
        return redisWrapperClient.exists(MessageFormat.format(LUXURY_STAGE_REPAY_EXPIRED_REDIS_KEY, String.valueOf(luxuryOrderId), String.valueOf(period)));
    }

    private boolean isFinished(long luxuryOrderId, int period) {
        try {
            String key = MessageFormat.format(LUXURY_STAGE_REPAY_REDIS_KEY, String.valueOf(luxuryOrderId), String.valueOf(period));
            return redisWrapperClient.exists(key) && SyncRequestStatus.valueOf(redisWrapperClient.get(key)) == SyncRequestStatus.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }
}
