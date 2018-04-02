package com.tuotiansudai.paywrapper.luxury;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.LuxuryStageRepayProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
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

    private final static String LUXURY_STAGE_REPAY_EXPIRED_REDIS_KEY = "luxury:stage:repay:expired:{0}";

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final UserMapper userMapper;

    private final AccountMapper accountMapper;

    private final PayAsyncClient payAsyncClient;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public LuxuryStageRepayService(UserMapper userMapper, AccountMapper accountMapper, PayAsyncClient payAsyncClient, MQWrapperClient mqWrapperClient) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.payAsyncClient = payAsyncClient;
        this.mqWrapperClient = mqWrapperClient;
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

        if (this.isRepaying(luxuryOrderId)) {
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
            redisWrapperClient.setex(MessageFormat.format(LUXURY_STAGE_REPAY_EXPIRED_REDIS_KEY, String.valueOf(luxuryOrderId)), 30 * 60, SyncRequestStatus.SENT.name());
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
                                period == 1 ? UserBillBusinessType.LUXURY_STAGE_PURCHASE : UserBillBusinessType.LUXURY_STAGE_REPAY,
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

    private boolean isRepaying(long luxuryOrderId) {
        return redisWrapperClient.exists(MessageFormat.format(LUXURY_STAGE_REPAY_EXPIRED_REDIS_KEY, String.valueOf(luxuryOrderId)));
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
}
