package com.tuotiansudai.log.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.model.Source;
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
        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setId(IdGenerator.generate());
        logModel.setLoginName(loginName);
        logModel.setMobile(userMapper.findByLoginName(loginName).getMobile());
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(userOpType);
        logModel.setCreatedTime(new Date());
        logModel.setDescription(description);

        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(logModel));
        } catch (JsonProcessingException e) {
            logger.error("[MQ] " + userOpType.getDesc() + ", send UserOperateLog fail.", e);
        }
    }
}
