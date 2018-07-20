package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.BankCardApplyNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.BankCardNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.PtpMerBindCardRequestModel;
import com.tuotiansudai.fudian.umpdto.UmpBindCardDto;
import com.tuotiansudai.fudian.umpmessage.UmpBindCardMessage;
import com.tuotiansudai.fudian.umpmessage.UmpRechargeMessage;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.fudian.util.UmpUtils;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UmpBindCardService {

    private static Logger logger = LoggerFactory.getLogger(UmpBindCardService.class);

    private final static String UMP_BIND_CARD_MESSAGE_KEY = "UMP_BIND_CARD_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private UmpBindCardService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public PtpMerBindCardRequestModel bindCard(UmpBindCardDto dto) {
        PtpMerBindCardRequestModel requestModel = new PtpMerBindCardRequestModel(String.valueOf(dto.getBankCardModelId()),
                dto.getCardNumber(),
                dto.getPayUserId(),
                dto.getUserName(),
                dto.getIdentityNumber(),
                dto.isFastPay());

        umpUtils.sign(requestModel);

        insertRequestMapper.insertCardBind(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP BIND CARD] failed to sign, data: {}", requestModel);
            return null;
        }

        UmpBindCardMessage umpBindCardMessage = new UmpBindCardMessage(dto.getBankCardModelId(), dto.getLoginName());
        String umpBindCardMessageKey = MessageFormat.format(UMP_BIND_CARD_MESSAGE_KEY, String.valueOf(umpBindCardMessage.getBindCardModelId()));
        redisTemplate.<String, String>opsForValue().set(umpBindCardMessageKey, gson.toJson(umpBindCardMessage), 1, TimeUnit.DAYS);
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        BankCardNotifyRequestModel bindCardNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, BankCardNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(bindCardNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyCardBind(bindCardNotifyModel);
        String umpBindCardMessageKey = MessageFormat.format(UMP_BIND_CARD_MESSAGE_KEY, String.valueOf(bindCardNotifyModel.getOrderId()));
        String message = redisTemplate.<String, String>opsForValue().get(umpBindCardMessageKey);
        UmpBindCardMessage umpBindCardMessage = gson.fromJson(message, UmpBindCardMessage.class);
        umpBindCardMessage.setStatus(bindCardNotifyModel.isSuccess());
        umpBindCardMessage.setApply(false);
        messageQueueClient.sendMessage(MessageQueue.UmpBindCard_Success, umpBindCardMessage);
        return bindCardNotifyModel.getResponseData();
    }

    public String applyNotifyCallBack(Map<String, String> paramsMap, String queryString){
        BankCardApplyNotifyRequestModel bindCardApplyNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, BankCardApplyNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(bindCardApplyNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertApplyNotifyCardBind(bindCardApplyNotifyModel);
        String umpBindCardMessageKey = MessageFormat.format(UMP_BIND_CARD_MESSAGE_KEY, String.valueOf(bindCardApplyNotifyModel.getOrderId()));
        String message = redisTemplate.<String, String>opsForValue().get(umpBindCardMessageKey);
        UmpBindCardMessage umpBindCardMessage = gson.fromJson(message, UmpBindCardMessage.class);
        umpBindCardMessage.setStatus(bindCardApplyNotifyModel.isSuccess());
        umpBindCardMessage.setApply(true);
        messageQueueClient.sendMessage(MessageQueue.UmpBindCard_Success, umpBindCardMessage);
        return bindCardApplyNotifyModel.getResponseData();
    }
}
