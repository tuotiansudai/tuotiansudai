package com.tuotiansudai.paywrapper.current;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CurrentRedeemService {

    private final static Logger logger = LoggerFactory.getLogger(CurrentRedeemService.class);

    private final static String ORDER_ID_SEPARATOR = "X";

    private final static String ORDER_ID_TEMPLATE = "{0}" + ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private AccountMapper accountMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private AmountTransfer amountTransfer;


    public BaseDto<PayDataDto> redeemToLoan(RedeemRequestDto redeemRequestDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        String loginName = redeemRequestDto.getLoginName();


        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return baseDto;
        }

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCurrentRedeemToLoanRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(redeemRequestDto.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(redeemRequestDto.getAmount()));

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);

            if (responseModel.isSuccess()) {
                logger.info("redeem to loan success, redeem_id:{}, login_name:{}, amount:{}", redeemRequestDto.getId(), loginName, String.valueOf(redeemRequestDto.getAmount()));
                amountTransfer.transferOutBalance(loginName, redeemRequestDto.getId(), redeemRequestDto.getAmount(), UserBillBusinessType.CURRENT_REDEEM_TO_LOAN, null, null);
                // TODO: call redeem_to_user method
            }

            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", String.valueOf(redeemRequestDto.getId()))
                    .build()));
        } catch (Exception e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(MessageFormat.format("redeem failed (id={0}, loginName={1}, amount={2}, source={3}",
                    String.valueOf(redeemRequestDto.getId()),
                    redeemRequestDto.getLoginName(),
                    String.valueOf(redeemRequestDto.getAmount()),
                    redeemRequestDto.getSource()), e);
        }

        return baseDto;
    }

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
