package com.tuotiansudai.paywrapper.current;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CurrentRedeemService {

    private final static Logger logger = Logger.getLogger(CurrentRedeemService.class);

    private final static String ORDER_ID_SEPARATOR = "X";

    private final static String ORDER_ID_TEMPLATE = "{0}" + ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private AccountMapper accountMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    // TODO: need to be call from callback of redeem_to_loan
    private BaseDto<PayDataDto> redeemToUser(String loginName, long redeemId, long redeemAmount) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return baseDto;
        }

        try {
            ProjectTransferRequestModel paybackRequestModel = ProjectTransferRequestModel.newCurrentRedeemToUserRequest(
                    MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(redeemId), String.valueOf(new Date().getTime())),
                    accountModel.getPayUserId(),
                    String.valueOf(redeemAmount));

            ProjectTransferResponseModel paybackResponseModel = this.paySyncClient.send(ProjectTransferMapper.class, paybackRequestModel, ProjectTransferResponseModel.class);
            if (paybackResponseModel.isSuccess()) {
                amountTransfer.transferInBalance(loginName, redeemId, redeemAmount, UserBillBusinessType.CURRENT_REDEEM_TO_USER, null, null);
                logger.info(MessageFormat.format("redeem to user success, redeem_to_user_request_id: {0}", String.valueOf(redeemId)));
            }
            String json = objectMapper.writeValueAsString(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("id", redeemId)
                    .put("status", paybackResponseModel.isSuccess() ? "SUCCESS" : "FAIL")
                    .build()));
            mqWrapperClient.sendMessage(MessageQueue.CurrentRedeemComplete, json);
        } catch (PayException | AmountTransferException e) {
            logger.error(MessageFormat.format("redeem to user failed, redeem_to_user_request_id: {0}", String.valueOf(redeemId)), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("日息宝赎回到用户失败,id: {0}", String.valueOf(redeemId)));
        } catch (JsonProcessingException e) {
            logger.error(MessageFormat.format("redeem to user failed, cause by build complete mq message failed, redeem_to_user_request_id: {0}", String.valueOf(redeemId)), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("日息宝赎回到用户成功, 发送mq通知失败, id: {0}", String.valueOf(redeemId)));
        }
        return baseDto;
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }
}
