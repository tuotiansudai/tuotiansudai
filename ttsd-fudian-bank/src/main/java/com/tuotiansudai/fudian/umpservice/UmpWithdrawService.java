package com.tuotiansudai.fudian.umpservice;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.mapper.ump.InsertNotifyMapper;
import com.tuotiansudai.fudian.mapper.ump.InsertRequestMapper;
import com.tuotiansudai.fudian.ump.asyn.callback.BaseCallbackRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.WithdrawApplyNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.callback.WithdrawNotifyRequestModel;
import com.tuotiansudai.fudian.ump.asyn.request.CustWithdrawalsRequestModel;
import com.tuotiansudai.fudian.umpdto.UmpWithdrawDto;
import com.tuotiansudai.fudian.umpmessage.UmpRechargeMessage;
import com.tuotiansudai.fudian.umpmessage.UmpWithdrawMessage;
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
public class UmpWithdrawService {

    private static Logger logger = LoggerFactory.getLogger(UmpWithdrawService.class);

    private final static String UMP_WITHDRAW_MESSAGE_KEY = "UMP_WITHDRAW_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final InsertRequestMapper insertRequestMapper;

    private final InsertNotifyMapper insertNotifyMapper;

    private final UmpUtils umpUtils;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private UmpWithdrawService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, InsertRequestMapper insertRequestMapper, UmpUtils umpUtils, InsertNotifyMapper insertNotifyMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.insertRequestMapper = insertRequestMapper;
        this.umpUtils = umpUtils;
        this.insertNotifyMapper = insertNotifyMapper;
    }

    public CustWithdrawalsRequestModel withdraw(UmpWithdrawDto dto) {
        CustWithdrawalsRequestModel requestModel = new CustWithdrawalsRequestModel(String.valueOf(dto.getWithdrawId()),
                dto.getPayUserId(),
                String.valueOf(dto.getAmount()));

        umpUtils.sign(requestModel);

        insertRequestMapper.insertWithdraw(requestModel);

        if (requestModel.getField().isEmpty()) {
            logger.error("[UMP WITHDRAW] failed to sign, data: {}", requestModel);
            return null;
        }

        UmpWithdrawMessage message = new UmpWithdrawMessage(dto.getWithdrawId(), dto.getLoginName(), dto.getAmount());
        String umpWithdrawMessageKey = MessageFormat.format(UMP_WITHDRAW_MESSAGE_KEY, String.valueOf(message.getWithdrawId()));
        redisTemplate.<String, String>opsForValue().set(umpWithdrawMessageKey, gson.toJson(message), 7, TimeUnit.DAYS);
        return requestModel;
    }

    public String notifyCallBack(Map<String, String> paramsMap, String queryString) {
        WithdrawNotifyRequestModel withdrawNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, WithdrawNotifyRequestModel.class);
        if (withdrawNotifyModel == null || Strings.isNullOrEmpty(withdrawNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertNotifyWithdraw(withdrawNotifyModel);
        this.sendMessage(withdrawNotifyModel, false);
        return withdrawNotifyModel.getResponseData();
    }

    public String applyNotifyCallBack(Map<String, String> paramsMap, String queryString) {
        WithdrawApplyNotifyRequestModel withdrawApplyNotifyModel = umpUtils.parseCallbackRequest(paramsMap, queryString, WithdrawApplyNotifyRequestModel.class);
        if (withdrawApplyNotifyModel == null || Strings.isNullOrEmpty(withdrawApplyNotifyModel.getResponseData())) {
            return null;
        }
        insertNotifyMapper.insertApplyNotifyWithdraw(withdrawApplyNotifyModel);
        this.sendMessage(withdrawApplyNotifyModel, true);
        return withdrawApplyNotifyModel.getResponseData();
    }

    private void sendMessage(BaseCallbackRequestModel model, boolean isApply){
        String umpWithdrawMessageKey = MessageFormat.format(UMP_WITHDRAW_MESSAGE_KEY, String.valueOf(model.getOrderId()));
        String message = redisTemplate.<String, String>opsForValue().get(umpWithdrawMessageKey);
        UmpWithdrawMessage umpWithdrawMessage = gson.fromJson(message, UmpWithdrawMessage.class);
        umpWithdrawMessage.setStatus(model.isSuccess());
        umpWithdrawMessage.setApply(isApply);
        umpWithdrawMessage.setNotifyMessage(model.getRetMsg());
        messageQueueClient.sendMessage(MessageQueue.UmpWithdraw_Success, umpWithdrawMessage);
    }
}
