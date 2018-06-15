package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuNopwdRepayMapper;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuRepayMapper;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuRepayNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.HuiZuRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.HuiZuRepayService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class HuizuRepayServiceImpl implements HuiZuRepayService {
    static Logger logger = Logger.getLogger(HuizuRepayServiceImpl.class);

    private static final String REPAY_PLAN_STATUS_KEY_TEMPLATE = "REPAY_PLAN_ID:%s";

    public final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "%s" + REPAY_ORDER_ID_SEPARATOR + "%s";

    private final static String RENT_REPAY_PLAN_EXPIRED_REDIS_KEY = "rent:repay:plan:expired:%s";

    private static final int REPAY_PAY_EXPIRE_SECOND = 60 * 60 * 24 * 30;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private HuiZuRepayNotifyRequestMapper huiZuRepayNotifyRequestMapper;
    @Value("${common.environment}")
    private Environment environment;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private MQWrapperClient mqWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();


    @Override
    public BaseDto<PayFormDataDto> passwordRepay(HuiZuRepayDto huiZuRepayDto) {
        logger.info(String.format("[HZ Password Repay:]%s-第%s期-还款金额:%s begin...",
                huiZuRepayDto.getMobile(),
                String.valueOf(huiZuRepayDto.getPeriod()),
                String.valueOf(huiZuRepayDto.getAmount())));
        UserModel userModel = userMapper.findByMobile(huiZuRepayDto.getMobile());
        AccountModel accountModel = accountMapper.lockByLoginName(userModel.getLoginName());
        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setCode(String.valueOf(HttpStatus.OK));
        dto.setData(payFormDataDto);

        if (AmountConverter.convertStringToCent(huiZuRepayDto.getAmount()) > accountModel.getBalance()) {
            logger.info(String.format("[HZ Password Repay:] id:%s 还款余额不足,余额:%s,实际还款:%s",
                    String.valueOf(huiZuRepayDto.getRepayPlanId()),
                    String.valueOf(accountModel.getBalance()),
                    String.valueOf(huiZuRepayDto.getAmount())));
            payFormDataDto.setMessage("余额不足，请充值");
            payFormDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return dto;
        }

        try {
            if (isPaying(Long.parseLong(huiZuRepayDto.getRepayPlanId()))) {
                payFormDataDto.setMessage("还款交易进行中, 请30分钟后查看");
                payFormDataDto.setCode(String.valueOf(HttpStatus.LOCKED));
                return dto;
            }

            if (isFinished(Long.parseLong(huiZuRepayDto.getRepayPlanId()))) {
                payFormDataDto.setMessage("您已还款成功");
                payFormDataDto.setCode(String.valueOf(HttpStatus.FORBIDDEN));
                return dto;
            }

            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newHuiZuRepayPasswordRequest(
                    String.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(huiZuRepayDto.getRepayPlanId()), String.valueOf(new DateTime().getMillis())),
                    accountModel.getPayUserId(),
                    String.valueOf(AmountConverter.convertStringToCent(huiZuRepayDto.getAmount())));

            redisWrapperClient.hmset(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, String.valueOf(huiZuRepayDto.getRepayPlanId())),
                    Maps.newHashMap(ImmutableMap.builder()
                            .put("mobile", huiZuRepayDto.getMobile())
                            .put("actual_amount", String.valueOf(AmountConverter.convertStringToCent(huiZuRepayDto.getAmount())))
                            .put("period", String.valueOf(huiZuRepayDto.getPeriod()))
                            .put("status", SyncRequestStatus.SENT.name())
                            .build()),
                    REPAY_PAY_EXPIRE_SECOND);
            redisWrapperClient.setex(String.format(RENT_REPAY_PLAN_EXPIRED_REDIS_KEY, String.valueOf(huiZuRepayDto.getRepayPlanId())), 60 * 30, SyncRequestStatus.SENT.name());
            dto = payAsyncClient.generateFormData(HuiZuRepayMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(String.format("[HZ Password Repay:] id:%s mobile:%s period:%s repay fail", String.valueOf(huiZuRepayDto.getRepayPlanId()), huiZuRepayDto.getMobile(), huiZuRepayDto.getPeriod()), e);
            payFormDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            payFormDataDto.setMessage("支付异常");
        }

        logger.info(String.format("[HZ Password Repay:]%s-第%s期-还款金额:%s end...",
                huiZuRepayDto.getMobile(),
                String.valueOf(huiZuRepayDto.getPeriod()),
                String.valueOf(huiZuRepayDto.getAmount())));
        return dto;
    }

    @Override
    public BaseDto<PayDataDto> noPasswordRepay(HuiZuRepayDto huiZuRepayDto) {
        logger.info(String.format("[HZ No Password Repay:]%s-第%s期-还款金额:%s begin...",
                huiZuRepayDto.getMobile(),
                String.valueOf(huiZuRepayDto.getPeriod()),
                String.valueOf(huiZuRepayDto.getAmount())));
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setCode(String.valueOf(HttpStatus.OK));
        baseDto.setData(payDataDto);

        UserModel userModel = userMapper.findByMobile(huiZuRepayDto.getMobile());
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());


        if (AmountConverter.convertStringToCent(huiZuRepayDto.getAmount()) > accountModel.getBalance()) {
            logger.info(String.format("[HZ No Password Repay:] id:%s 还款余额不足,余额:%s,实际还款:%s",
                    String.valueOf(huiZuRepayDto.getRepayPlanId()),
                    String.valueOf(accountModel.getBalance()),
                    String.valueOf(huiZuRepayDto.getAmount())));
            payDataDto.setMessage("余额不足，请充值");
            payDataDto.setCode(String.valueOf(HttpStatus.PRECONDITION_REQUIRED));
            return baseDto;
        }
        try {

            if (isPaying(Long.parseLong(huiZuRepayDto.getRepayPlanId()))) {
                payDataDto.setMessage("还款交易进行中, 请30分钟后查看");
                payDataDto.setCode(String.valueOf(HttpStatus.LOCKED));
                return baseDto;
            }

            if (isFinished(Long.parseLong(huiZuRepayDto.getRepayPlanId()))) {
                payDataDto.setMessage("您已还款成功");
                payDataDto.setCode(String.valueOf(HttpStatus.FORBIDDEN));
                return baseDto;
            }

            ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newHuiZuRepayNopwdRequest(
                    String.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(huiZuRepayDto.getRepayPlanId()), String.valueOf(new DateTime().getMillis())),
                    accountModel.getPayUserId(),
                    String.valueOf(AmountConverter.convertStringToCent(huiZuRepayDto.getAmount())));

            redisWrapperClient.hmset(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, String.valueOf(huiZuRepayDto.getRepayPlanId())),
                    Maps.newHashMap(ImmutableMap.builder()
                            .put("mobile", huiZuRepayDto.getMobile())
                            .put("actual_amount", String.valueOf(AmountConverter.convertStringToCent(huiZuRepayDto.getAmount())))
                            .put("period", String.valueOf(huiZuRepayDto.getPeriod()))
                            .put("status", SyncRequestStatus.SENT.name())
                            .build()),
                    REPAY_PAY_EXPIRE_SECOND);

            redisWrapperClient.setex(String.format(RENT_REPAY_PLAN_EXPIRED_REDIS_KEY, String.valueOf(huiZuRepayDto.getRepayPlanId())), 60 * 30, SyncRequestStatus.SENT.name());

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    HuiZuNopwdRepayMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("callbackUrl", requestModel.getRetUrl())
                    .build()));

        } catch (PayException e) {
            logger.error(String.format("[HZ No Password Repay:] id:%s mobile:%s period:%s repay fail", String.valueOf(huiZuRepayDto.getRepayPlanId()), huiZuRepayDto.getMobile(), huiZuRepayDto.getPeriod()), e);
            payDataDto.setStatus(false);
            payDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            payDataDto.setMessage("支付异常");
            redisWrapperClient.hset(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, huiZuRepayDto.getRepayPlanId()), "status", SyncRequestStatus.FAILURE.name());
        }

        logger.info(String.format("[HZ No Password Repay:]%s-第%s期-还款金额:%s end...",
                huiZuRepayDto.getMobile(),
                String.valueOf(huiZuRepayDto.getPeriod()),
                String.valueOf(huiZuRepayDto.getAmount())));

        return baseDto;
    }

    @Override
    public String huiZuRepayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                HuiZuRepayNotifyRequestMapper.class,
                HuiZuRepayNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(String.format("[HZ Password Repay:] HZ Password Repay callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }
        hzRepayModify(callbackRequest.getId());
        return callbackRequest.getResponseData();
    }

    @Override
    public void hzRepayModify(long notifyRequestId) {


        HuiZuRepayNotifyRequestModel model = huiZuRepayNotifyRequestMapper.findById(notifyRequestId);
        String orderId = model.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0];
        if (model != null && NotifyProcessStatus.NOT_DONE.name().equals(model.getStatus())) {
            if (!SyncRequestStatus.SENT.name().equals(redisWrapperClient.hget(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "status"))) {
                logger.error(String.format("[HZ Password Repay:] ID:%s status is %s ",
                        orderId,
                        redisWrapperClient.hget(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "status")));
                return;
            }
            logger.info(String.format("[HZ Password Repay:] %s starting...", orderId));
            if (updateHuiZuNotifyRequestNotifyRequestStatus(model)) {
                try {
                    redisWrapperClient.hset(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "status", model.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                    String actualAmount = redisWrapperClient.hget(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "actual_amount");
                    mqWrapperClient.sendMessage(MessageQueue.HuiZuRentRepayNotifyQueue, Maps.newHashMap(ImmutableMap.<String, Object>builder()
                            .put("orderId", orderId)
                            .put("actual_amount", actualAmount)
                            .put("status", model.isSuccess())
                            .build()));
                    if (model.isSuccess()) {
                        this.postRepay(orderId);
                        logger.info(MessageFormat.format("[HZ Password Repay:] update huizu balance and user bill", String.valueOf(orderId)));
                    } else {
                        logger.error(MessageFormat.format("[HZ Password Repay:] update huizu repay fail", String.valueOf(orderId)));
                    }
                } catch (Exception e) {
                    String errMsg = MessageFormat.format("[HZ Password Repay:] hzRepayModify error. ID:{0}", orderId);
                    logger.error(errMsg, e);
                    sendFatalNotify(MessageFormat.format("慧租还款回调处理错误。{0},{1}", environment, errMsg));
                }
            }
        }
    }

    @Override
    @Transactional
    public void postRepay(String orderId) throws AmountTransferException {
        String mobile = redisWrapperClient.hget(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "mobile");
        UserModel userModel = userMapper.findByMobile(mobile);
        AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, userModel.getLoginName(),
                Long.parseLong(orderId),
                Long.parseLong(redisWrapperClient.hget(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "actual_amount")), UserBillBusinessType.HUI_ZU_REPAY_IN, null, null);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

        mqWrapperClient.sendMessage(MessageQueue.CreditLoanBill,
                new CreditLoanBillModel(Long.parseLong(orderId),
                        Long.parseLong(redisWrapperClient.hget(String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, orderId), "actual_amount")),
                        CreditLoanBillOperationType.IN, CreditLoanBillBusinessType.YOOCAR_LOAN_REPAY, mobile));

    }

    private void sendFatalNotify(String message) {
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, message);
    }

    private boolean updateHuiZuNotifyRequestNotifyRequestStatus(HuiZuRepayNotifyRequestModel model) {
        try {
            huiZuRepayNotifyRequestMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
            logger.info(String.format("[HZ Password Repay:] %s  update request status to DONE", model.getOrderId()));
        } catch (Exception e) {
            logger.error(String.format("[HZ Password Repay:] %s update request status is failed", model.getOrderId()), e);
            this.sendFatalNotify(String.format("[HZ Password Repay:] %s 慧租还款 回调状态更新错误", model.getOrderId()));
            return false;
        }
        return true;
    }

    private boolean isPaying(long orderId) {
        try {
            String key = String.format(RENT_REPAY_PLAN_EXPIRED_REDIS_KEY, String.valueOf(orderId));
            return redisWrapperClient.exists(key) && SyncRequestStatus.valueOf(redisWrapperClient.get(key)) == SyncRequestStatus.SENT;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private boolean isFinished(long orderId) {
        try {
            String key = String.format(REPAY_PLAN_STATUS_KEY_TEMPLATE, String.valueOf(orderId));
            return redisWrapperClient.exists(key) && SyncRequestStatus.valueOf(redisWrapperClient.hget(key, "status")) == SyncRequestStatus.SUCCESS;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }


}
