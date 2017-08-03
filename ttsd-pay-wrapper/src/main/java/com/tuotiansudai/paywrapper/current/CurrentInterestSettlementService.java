package com.tuotiansudai.paywrapper.current;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.current.dto.InterestSettlementRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CurrentInterestSettlementService {

    private final static Logger logger = Logger.getLogger(CurrentInterestSettlementService.class);

    private final static String ORDER_ID_SEPARATOR = "X";

    private final static String ORDER_ID_TEMPLATE = "{0}" + ORDER_ID_SEPARATOR + "{1}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    public BaseDto<PayDataDto> InterestSettlement(InterestSettlementRequestDto interestSettlementRequestDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);
        //此账户需要提前准备好（特殊的个人账号）
        String loginName = interestSettlementRequestDto.getLoginName();
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return baseDto;
        }

        String order_id = String.valueOf(new Date().getTime());
        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCurrentInterestSettlementRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(accountModel.getId()), order_id),
                accountModel.getPayUserId(),
                String.valueOf(interestSettlementRequestDto.getAmount()));

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", order_id)
                    .build()));

            if (responseModel.isSuccess()) {
                amountTransfer.transferOutBalance(loginName, Long.parseLong(order_id), interestSettlementRequestDto.getAmount(), UserBillBusinessType.CURRENT_INTEREST_TO_LOAN, null, null);
                logger.info(MessageFormat.format("interest to loan success, interest_to_loan_request_id: {0}", order_id));
            }
            String json = objectMapper.writeValueAsString(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("id", order_id)
                    .put("status", responseModel.isSuccess() ? "SUCCESS" : "FAIL")
                    .build()));
        } catch (PayException | AmountTransferException e) {
            logger.error(MessageFormat.format("interest to loan failed, interest_to_loan_request_id: {0}", order_id), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("利息结算到标的账户失败,id: {0}", order_id));
        } catch (JsonProcessingException e) {
            logger.error(MessageFormat.format("interest to loan failed, cause by build complete mq message failed, interest_to_loan_request_id: {0}", order_id), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("利息结算到标的账户成功, 发送mq通知失败, id: {0}", order_id));
        }
        return baseDto;
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

}
