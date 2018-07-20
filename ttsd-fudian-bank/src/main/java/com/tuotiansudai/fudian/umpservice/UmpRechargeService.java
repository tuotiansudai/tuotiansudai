package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.fudian.ump.asyn.callback.RechargeNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.MerRechargePersonRequestModel;
import com.tuotiansudai.fudian.umpdto.UmpRechargeDto;
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
public class UmpRechargeService {

    private static Logger logger = LoggerFactory.getLogger(UmpRechargeService.class);

    private final static String UMP_RECHARGE_MESSAGE_KEY = "UMP_RECHARGE_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private UmpRechargeService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public MerRechargePersonRequestModel recharge(UmpRechargeDto dto) {
        MerRechargePersonRequestModel model = MerRechargePersonRequestModel.newRecharge(String.valueOf(dto.getRechargeId()),
                dto.getPayUserId(),
                String.valueOf(dto.getAmount()),
                dto.getBankCode());

        if (dto.isFastPay()) {
            model = MerRechargePersonRequestModel.newFastRecharge(String.valueOf(dto.getRechargeId()),
                    dto.getPayUserId(),
                    String.valueOf(dto.getAmount()));
        }

        umpUtils.sign(model);

        insertRequestMapper.insertRecharge(model);

        if (model.getField().isEmpty()) {
            logger.error("[UMP RECHARGE] failed to sign, data: {}", model);
            return null;
        }
        UmpRechargeMessage umpRechargeMessage = new UmpRechargeMessage(dto.getRechargeId(), dto.getLoginName(), dto.getAmount());

        String umpRechargeMessageKey = MessageFormat.format(UMP_RECHARGE_MESSAGE_KEY, String.valueOf(umpRechargeMessage.getRechargeId()));
        redisTemplate.<String, String>opsForValue().set(umpRechargeMessageKey, gson.toJson(umpRechargeMessage), 7, TimeUnit.DAYS);

        return model;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString){
        RechargeNotifyRequestModel model = umpUtils.parseCallbackRequest(paramsMap, queryString, RechargeNotifyRequestModel.class);
        if (Strings.isNullOrEmpty(model.getResponseData())){
            return null;
        }
        insertNotifyMapper.insertNotifyRecharge(model);
        String umpRechargeMessageKey = MessageFormat.format(UMP_RECHARGE_MESSAGE_KEY, String.valueOf(model.getOrderId()));
        String message = redisTemplate.<String, String>opsForValue().get(umpRechargeMessageKey);
        UmpRechargeMessage umpRechargeMessage = gson.fromJson(message, UmpRechargeMessage.class);
        umpRechargeMessage.setStatus(model.isSuccess());
        messageQueueClient.sendMessage(MessageQueue.UmpRecharge_Success, message);

        return model.getResponseData();
    }

}
