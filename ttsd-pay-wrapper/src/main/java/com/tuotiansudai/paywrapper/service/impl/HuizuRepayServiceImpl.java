package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuRepayMapper;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuRepayNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.HuiZuRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.service.HuiZuRepayService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class HuizuRepayServiceImpl implements HuiZuRepayService {
    static Logger logger = Logger.getLogger(HuizuRepayServiceImpl.class);

    private static final String REPAY_LOAN_ID = "88888";

    public final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "%s" + REPAY_ORDER_ID_SEPARATOR + "%s";

    private static final int REPAY_PAY_EXPIRE_SECOND = 60 * 60 * 24 * 30;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private SmsWrapperClient smsWrapperClient;
    @Autowired
    private HuiZuRepayNotifyRequestMapper huiZuRepayNotifyRequestMapper;
    @Value("${common.environment}")
    private Environment environment;
    @Autowired
    private AmountTransfer amountTransfer;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();


    @Override
    public BaseDto<PayFormDataDto> passwordRepay(HuiZuRepayDto huiZuRepayDto) {
        logger.info(String.format("[HZ Repay:]%s-第%s期-还款金额:%s begin...",
                huiZuRepayDto.getLoginName(),
                String.valueOf(huiZuRepayDto.getPeriod()),
                String.valueOf(huiZuRepayDto.getAmount())));

        AccountModel accountModel = accountMapper.lockByLoginName(huiZuRepayDto.getLoginName());
        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        dto.setData(payFormDataDto);

        if (AmountConverter.convertStringToCent(huiZuRepayDto.getAmount()) > accountModel.getBalance()) {
            logger.info(String.format("[HZ Repay:] id:%s 还款余额不足,余额:%s,实际还款:%s",
                    String.valueOf(huiZuRepayDto.getRepayPlanId()),
                    String.valueOf(accountModel.getBalance()),
                    String.valueOf(huiZuRepayDto.getAmount())));
            payFormDataDto.setMessage("余额不足，请充值");
            return dto;
        }

        try {
            if (redisWrapperClient.exists(String.format("REPAY_PLAN_ID:%s", huiZuRepayDto.getRepayPlanId()))) {
                payFormDataDto.setMessage(String.format("用户:%s-第%s期-已经还款成功",
                        huiZuRepayDto.getLoginName(),
                        String.valueOf(huiZuRepayDto.getPeriod())));
                return dto;
            }

            redisWrapperClient.hmset(String.format("REPAY_PLAN_ID:%s", String.valueOf(huiZuRepayDto.getRepayPlanId())),
                    Maps.newHashMap(ImmutableMap.builder()
                            .put("loginName", huiZuRepayDto.getLoginName())
                            .put("amount", String.valueOf(AmountConverter.convertStringToCent(huiZuRepayDto.getAmount())))
                            .put("period", String.valueOf(huiZuRepayDto.getPeriod()))
                            .put("status", SyncRequestStatus.SENT.name())
                            .build()),
                    REPAY_PAY_EXPIRE_SECOND);
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newHuiZuRepayPasswordRequest(
                    REPAY_LOAN_ID,
                    String.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(huiZuRepayDto.getRepayPlanId()), String.valueOf(new DateTime().getMillis())),
                    accountModel.getPayUserId(),
                    String.valueOf(huiZuRepayDto.getAmount()));
            return payAsyncClient.generateFormData(HuiZuRepayMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(String.format("[HZ Repay:] id:{} loginName:{} period:{} repay fail"), e);
        }

        logger.info(String.format("[HZ Repay:]%s-第%s期-还款金额:%s end...",
                huiZuRepayDto.getLoginName(),
                String.valueOf(huiZuRepayDto.getPeriod()),
                String.valueOf(huiZuRepayDto.getAmount())));
        return dto;
    }

    @Override
    public String huiZuRepayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                HuiZuRepayNotifyRequestMapper.class,
                HuiZuRepayNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(String.format("[HZ Repay:] HZ REPAY callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }
        hzRepayModify(callbackRequest.getId());
        return callbackRequest.getResponseData();
    }

    @Override
    public void hzRepayModify(long notifyRequestId) {


        HuiZuRepayNotifyRequestModel model = huiZuRepayNotifyRequestMapper.findById(notifyRequestId);
        String orderId = model.getOrderId().split("REPAY_ORDER_ID_SEPARATOR")[0];
        if (model != null && NotifyProcessStatus.NOT_DONE.name().equals(model.getStatus())) {
            if (!SyncRequestStatus.SENT.name().equals(redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", orderId), "status"))) {
                logger.error(String.format("[HZ Repay:] ID:%s status is %s ",
                        orderId,
                        redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", orderId), "status")));
                return;
            }
            logger.info(String.format("[HZ Repay:] %s starting...", orderId));
            if (updateHuiZuNotifyRequestNotifyRequestStatus(model)) {
                try {
                    if (model.isSuccess()) {
                        redisWrapperClient.hset(String.format("REPAY_PLAN_ID:%s", orderId), "status", SyncRequestStatus.SUCCESS.name());
                        this.postRepay(orderId);
                        logger.info(MessageFormat.format("[HZ Repay:] update huizu balance and user bill", String.valueOf(orderId)));
                    } else {
                        redisWrapperClient.hset(String.format("REPAY_PLAN_ID:%s", orderId), "status", SyncRequestStatus.FAILURE.name());
                        logger.error(MessageFormat.format("[HZ Repay:] update huizu repay fail", String.valueOf(orderId)));
                    }
                } catch (Exception e) {
                    String errMsg = MessageFormat.format("[HZ Repay:] hzRepayModify error. ID:{0}", orderId);
                    logger.error(errMsg, e);
                    sendFatalNotify(MessageFormat.format("慧租还款回调处理错误。{0},{1}", environment, errMsg));
                }
            }
        }
    }

    @Override
    @Transactional
    public void postRepay(String orderId) throws AmountTransferException {
        amountTransfer.transferOutBalance(redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", orderId), "loginName"),
                Long.parseLong(orderId),
                Long.parseLong(redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", orderId), "amount")), UserBillBusinessType.HUI_ZU_REPAY_IN, null, null);

        //TODO: modify loan_bill 流水数据
        //TODO:send MQ message to huizu 更新RepayPlan的status
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

    private boolean updateHuiZuNotifyRequestNotifyRequestStatus(HuiZuRepayNotifyRequestModel model) {
        try {
            huiZuRepayNotifyRequestMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
            logger.info(String.format("[HZ Repay:] %s  update request status to DONE", model.getOrderId()));
        } catch (Exception e) {
            logger.error(String.format("[HZ Repay:] %s update request status is failed", model.getOrderId()), e);
            this.sendFatalNotify(String.format("[HZ Repay:] %s 慧租还款 回调状态更新错误", model.getOrderId()));
            return false;
        }
        return true;
    }


}
