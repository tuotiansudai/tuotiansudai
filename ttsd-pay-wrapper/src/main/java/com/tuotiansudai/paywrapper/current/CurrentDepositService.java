package com.tuotiansudai.paywrapper.current;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentDepositNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CurrentDepositNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class CurrentDepositService {

    private final static Logger logger = Logger.getLogger(CurrentDepositService.class);

    private final static String ORDER_ID_SEPARATOR = "X";

    private final static String ORDER_ID_TEMPLATE = "{0}" + ORDER_ID_SEPARATOR + "{1}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    public BaseDto<PayFormDataDto> deposit(DepositRequestDto depositRequestDto) {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);

        String loginName = depositRequestDto.getLoginName();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return dto;
        }

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCurrentDepositRequest(
                    MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(depositRequestDto.getId()), String.valueOf(new Date().getTime())),
                    accountModel.getPayUserId(),
                    String.valueOf(depositRequestDto.getAmount()),
                    depositRequestDto.getSource());
            return payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("deposit failed (id={0}, loginName={1}, amount={2}, source={3}",
                    String.valueOf(depositRequestDto.getId()),
                    depositRequestDto.getLoginName(),
                    String.valueOf(depositRequestDto.getAmount()),
                    depositRequestDto.getSource()), e);
        }

        return dto;
    }

    public BaseDto<PayDataDto> noPasswordDeposit(DepositRequestDto depositRequestDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        String loginName = depositRequestDto.getLoginName();
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return baseDto;
        }

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCurrentDepositNopwdRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(depositRequestDto.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(depositRequestDto.getAmount()));

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", String.valueOf(depositRequestDto.getId()))
                    .build()));
        } catch (PayException e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    public String depositCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CurrentDepositNotifyRequestMapper.class,
                CurrentDepositNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }


        try {
            long orderId = Long.parseLong(callbackRequest.getOrderId().split(ORDER_ID_SEPARATOR)[0]);
            String json = objectMapper.writeValueAsString(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                    .put("order_id", orderId)
                    .put("success", callbackRequest.isSuccess())
                    .build()));
            mqWrapperClient.sendMessage(MessageQueue.CurrentDepositCallback, json);
        } catch (Exception e) {
            logger.error(MessageFormat.format("send deposit callback message error, order id {0}", callbackRequest.getOrderId()), e);
        }

        return callbackRequest.getResponseData();
    }
}
