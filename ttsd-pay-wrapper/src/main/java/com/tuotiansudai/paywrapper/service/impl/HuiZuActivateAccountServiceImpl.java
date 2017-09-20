package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuActivateAccountNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.HuiZuActivateAccountNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.HuiZuActivateAccountService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
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
public class HuiZuActivateAccountServiceImpl implements HuiZuActivateAccountService {

    static Logger logger = Logger.getLogger(HuiZuActivateAccountServiceImpl.class);

    private final static int ACTIVATE_ACCOUNT_MONEY = 1;

    private static final String LOAN_ID = "58";

    private final static String ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR = "X";

    private final static String ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE = "{0}" + ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;


    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    private HuiZuActivateAccountMapper huiZuActivateAccountMapper;

    @Autowired
    private HuiZuActivateAccountNotifyMapper huiZuActivateAccountNotifyMapper;

    @Override
    public BaseDto<PayDataDto> noPassword(HuiZuActivateAccountDto activateAccountDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        UserModel userModel = userMapper.findByMobile(activateAccountDto.getMobile());
        AccountModel accountModel = accountMapper.lockByLoginName(userModel.getLoginName());
        String orderId = String.valueOf(IdGenerator.generate());

        if (ACTIVATE_ACCOUNT_MONEY > accountModel.getBalance()) {
            payDataDto.setMessage("余额不足，请充值");
            return baseDto;
        }

        try {

            if (redisWrapperClient.exists(String.format("ACTIVATE_ACCOUNT_MOBILE::%s", activateAccountDto.getMobile()))) {
                payDataDto.setMessage(String.format("用户%s:已经激活过账户", activateAccountDto.getMobile()));
                return baseDto;
            }

            redisWrapperClient.hmset(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", activateAccountDto.getMobile()),
                    Maps.newHashMap(ImmutableMap.builder()
                            .put("mobile", activateAccountDto.getMobile())
                            .put("amount", String.valueOf(ACTIVATE_ACCOUNT_MONEY))
                            .put("status", SyncRequestStatus.SENT.name())
                            .build()));

            logger.info(MessageFormat.format("[Activate Account Request Data] mobile={0}, loan={1}, orderId={2}, amount={3}, source={4}",
                    activateAccountDto.getMobile(),
                    LOAN_ID,
                    String.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, String.valueOf(activateAccountDto.getMobile()), String.valueOf(new DateTime().getMillis())),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY),
                    activateAccountDto.getSource()));

            ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newHuiZuActivateAccountNopwdRequest(
                    LOAN_ID,
                    String.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, String.valueOf(activateAccountDto.getMobile()), String.valueOf(new DateTime().getMillis())),
                    accountModel.getPayUserId(),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY));

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", orderId)
                    .build()));
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> password(HuiZuActivateAccountDto activateAccountDto) {
        //TODO: verify transfer

        UserModel userModel = userMapper.findByMobile(activateAccountDto.getMobile());
        AccountModel accountModel = accountMapper.lockByLoginName(userModel.getLoginName());
        String orderId = String.valueOf(IdGenerator.generate());

        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        dto.setData(payFormDataDto);

        if (ACTIVATE_ACCOUNT_MONEY > accountModel.getBalance()) {
            payFormDataDto.setMessage("余额不足，请充值");
            return dto;
        }

        try {
            if (redisWrapperClient.exists(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", activateAccountDto.getMobile()))) {
                payFormDataDto.setMessage(String.format("用户%s:已经激活过账户",
                        activateAccountDto.getMobile()));
                return dto;
            }

            redisWrapperClient.hmset(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", activateAccountDto.getMobile()),
                    Maps.newHashMap(ImmutableMap.builder()
                            .put("mobile", activateAccountDto.getMobile())
                            .put("amount", String.valueOf(ACTIVATE_ACCOUNT_MONEY))
                            .put("status", SyncRequestStatus.SENT.name())
                            .build()));

            logger.info(MessageFormat.format("[Activate Account Request Data] mobile={0}, loan={1}, orderId={2}, amount={3}, source={4}",
                    activateAccountDto.getMobile(),
                    LOAN_ID,
                    String.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, String.valueOf(activateAccountDto.getMobile()), String.valueOf(new DateTime().getMillis())),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY),
                    activateAccountDto.getSource()));

            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newHuiZuActivateAccountRequest(
                    String.valueOf(LOAN_ID),
                    String.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, String.valueOf(activateAccountDto.getMobile()), String.valueOf(new DateTime().getMillis())),
                    accountModel.getPayUserId(),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY));
            return payAsyncClient.generateFormData(HuiZuActivateAccountMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("{0} Activate Account is failed", activateAccountDto.getMobile()), e);
        }

        return dto;
    }

    @Override
    public String activateAccountCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                HuiZuActivateAccountNotifyMapper.class,
                HuiZuActivateAccountNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(String.format("[HZ Activate Account:] HZ Activate Account callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }
        activateAccount(callbackRequest.getId());
//        mqWrapperClient.sendMessage(MessageQueue.TransferInvestCallback, String.valueOf(callbackRequest.getId()));
        return callbackRequest.getResponseData();
    }

    private void activateAccount(long notifyRequestId) {
        HuiZuActivateAccountNotifyRequestModel model = huiZuActivateAccountNotifyMapper.findById(notifyRequestId);

        if (model == null) {
            logger.error(MessageFormat.format("惠租一分钱激活后账户回调处理错误。{0},{1} not found", environment, String.valueOf(notifyRequestId)));
            sendFatalNotify(MessageFormat.format("惠租一分钱激活后账户回调处理错误。{0},{1} not found", environment, String.valueOf(notifyRequestId)));
        }

        String orderId = model.getOrderId().split("ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR")[0];

        if (model != null && NotifyProcessStatus.NOT_DONE.name().equals(model.getStatus())) {
            if (!SyncRequestStatus.SENT.name().equals(redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", orderId), "status"))) {
                logger.error(String.format("[HZ Activate Account:] mobile:%s status is %s ",
                        orderId,
                        redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", orderId), "status")));
                return;
            }

            logger.info(MessageFormat.format("[Activate Account Callback {0}] starting...", model.getOrderId()));
            if (updateActivateAccountNotifyRequestStatus(model)) {
                try {
                    this.postActivateAccount(Long.parseLong(orderId));
                } catch (Exception e) {
                    String errMsg = MessageFormat.format("Activate Account, processOneCallback error. orderId:{0}", model.getOrderId());
                    logger.error(errMsg, e);
                    sendFatalNotify(MessageFormat.format("惠租账号激活状态回调处理错误。{0},{1}", environment, errMsg));
                }
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postActivateAccount(long orderId) throws AmountTransferException {

        String mobile = redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", orderId), "mobile");
        UserModel userModel = userMapper.findByMobile(mobile);
        // generate transferee balance
        amountTransfer.transferOutBalance(userModel.getLoginName(),orderId, Long.parseLong(redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", orderId), "amount")), UserBillBusinessType.HUI_ZU_ACTIVATE_ACCOUNT, null, null);
        logger.info(MessageFormat.format("[activate account Callback {0}] update balance and user bill", String.valueOf(orderId)));

    }

    private boolean updateActivateAccountNotifyRequestStatus(HuiZuActivateAccountNotifyRequestModel model) {
        try {
            huiZuActivateAccountNotifyMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
            logger.info(MessageFormat.format("[Activate Account Callback {0}] decrease request count and update request status to DONE", model.getOrderId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Activate Account Callback {0}] update request status is failed", model.getOrderId()), e);
            this.sendFatalNotify(MessageFormat.format("惠租一分钱激活账户({0})回调状态更新错误", model.getOrderId()));
            return false;
        }
        return true;
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }
}
