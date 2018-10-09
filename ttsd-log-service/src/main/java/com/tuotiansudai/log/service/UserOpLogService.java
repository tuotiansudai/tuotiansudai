package com.tuotiansudai.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.UserOpLogMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class UserOpLogService {

    private static Logger logger = Logger.getLogger(UserOpLogService.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

    public void sendUserOpLogMQ(String loginName, String ip, String platform, String deviceId, UserOpType userOpType, String description) {
        UserOpLogMessage logMessage = new UserOpLogMessage();
        logMessage.setId(IdGenerator.generate());
        logMessage.setLoginName(loginName);
        logMessage.setMobile(getMobile(loginName));
        logMessage.setIp(ip);
        logMessage.setDeviceId(deviceId);
        logMessage.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logMessage.setOpType(userOpType);
        logMessage.setCreatedTime(new Date());
        logMessage.setDescription(description);

        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(logMessage));
        } catch (JsonProcessingException e) {
            logger.error("[MQ] " + userOpType.getDesc() + ", send UserOperateLog fail.", e);
        }
    }

    private String getMobile(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel != null ? userModel.getMobile() : "";
    }
}
