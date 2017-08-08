package com.tuotiansudai.paywrapper.current;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.DepositDto;
import com.tuotiansudai.current.dto.DepositStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentDepositNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentDepositRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentOverDepositNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentOverDepositRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
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

    @Autowired
    private CurrentRestClient currentRestClient;

    public BaseDto<PayFormDataDto> deposit(DepositDto depositRequestDto) {
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
            return payAsyncClient.generateFormData(CurrentDepositRequestMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("deposit failed (id={0}, loginName={1}, amount={2}, source={3}",
                    String.valueOf(depositRequestDto.getId()),
                    depositRequestDto.getLoginName(),
                    String.valueOf(depositRequestDto.getAmount()),
                    depositRequestDto.getSource()), e);
        }

        return dto;
    }

    public BaseDto<PayDataDto> noPasswordDeposit(DepositDto depositRequestDto) {
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
            ProjectTransferResponseModel responseModel = paySyncClient.send(CurrentDepositRequestMapper.class,
                    requestModel,
                    ProjectTransferResponseModel.class);
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

    public void overDeposit(DepositDto depositRequestDto) {
        try {
            DepositDto deposit = currentRestClient.getDeposit(depositRequestDto.getId());
            if (deposit.getStatus() != DepositStatus.OVER_PAY) {
                logger.error(MessageFormat.format("deposit({0}) does not exist or status is not OVER_PAY", String.valueOf(depositRequestDto.getId())));
                return;
            }
        } catch (RestException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        String loginName = depositRequestDto.getLoginName();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return;
        }

        ProjectTransferRequestModel paybackRequestModel = ProjectTransferRequestModel.newCurrentOverDepositPaybackRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(depositRequestDto.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(depositRequestDto.getAmount()));

        try {
            depositRequestDto.setStatus(DepositStatus.APPLYING_PAYBACK);
            currentRestClient.updateDeposit(depositRequestDto.getId(), depositRequestDto);
            ProjectTransferResponseModel paybackResponseModel = this.paySyncClient.send(CurrentOverDepositRequestMapper.class, paybackRequestModel, ProjectTransferResponseModel.class);
            logger.info(MessageFormat.format("deposit({0}) apply payback ump code is {1}", String.valueOf(depositRequestDto.getId()), paybackResponseModel.getRetCode()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("deposit({0}) apply payback failed", String.valueOf(depositRequestDto.getId())) , e);
        }
    }

    public String depositCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CurrentDepositNotifyRequestMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            long orderId = Long.parseLong(callbackRequest.getOrderId().split(ORDER_ID_SEPARATOR)[0]);
            DepositDto deposit = currentRestClient.getDeposit(orderId);
            deposit.setStatus(callbackRequest.isSuccess() ? DepositStatus.SUCCESS : DepositStatus.FAIL);
            if (!callbackRequest.isSuccess()) {
                logger.error(MessageFormat.format("deposit({0}) callback notify failed", String.valueOf(orderId)));
            }

            String json = objectMapper.writeValueAsString(deposit);
            mqWrapperClient.sendMessage(MessageQueue.CurrentDepositCallback, json);
        } catch (Exception e) {
            logger.error(MessageFormat.format("send deposit callback message error, order id {0}", callbackRequest.getOrderId()), e);
        }

        return callbackRequest.getResponseData();
    }

    public String overDepositCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CurrentOverDepositNotifyRequestMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            long orderId = Long.parseLong(callbackRequest.getOrderId().split(ORDER_ID_SEPARATOR)[0]);
            DepositDto deposit = currentRestClient.getDeposit(orderId);
            deposit.setStatus(callbackRequest.isSuccess() ? DepositStatus.PAYBACK_SUCCESS : DepositStatus.PAYBACK_FAIL);
            if (!callbackRequest.isSuccess()) {
                logger.error(MessageFormat.format("deposit({0}) payback callback notify failed", String.valueOf(orderId)));
            }

            String json = objectMapper.writeValueAsString(deposit);
            mqWrapperClient.sendMessage(MessageQueue.CurrentDepositCallback, json);
        } catch (Exception e) {
            logger.error(MessageFormat.format("send deposit callback message error, order id {0}", callbackRequest.getOrderId()), e);
        }

        return callbackRequest.getResponseData();
    }
}
