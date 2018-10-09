package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.message.EMailMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.UserOpLogMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BindEmailService;
import com.tuotiansudai.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class BindEmailServiceImpl implements BindEmailService {

    static Logger logger = Logger.getLogger(BindEmailServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${web.server}")
    private String webServer;

    private final static String ACTIVE_URL_TEMPLATE = "{webServer}/bind-email/verify/{uuid}";

    @Override
    public boolean sendActiveEmail(String loginName, String email, String url) {
        if (Strings.isNullOrEmpty(email)) {
            return false;
        }

        String uuid = UUIDGenerator.generate();
        String bindEmailKey = "web:{loginName}:{uuid}";
        String bindEmailValue = "{loginName}:{email}";
        String activeUrl = ACTIVE_URL_TEMPLATE.replace("{webServer}", webServer).replace("{uuid}", uuid);

        redisWrapperClient.setex(bindEmailKey.replace("{loginName}", loginName).replace("{uuid}", uuid),
                86400,
                bindEmailValue.replace("{loginName}", loginName).replace("{email}", email));

        Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                .put("loginName", loginName)
                .put("activeUrl", activeUrl)
                .build());

        mqWrapperClient.sendMessage(MessageQueue.EMailMessage, new EMailMessage(Lists.newArrayList(email),
                SendCloudTemplate.ACTIVE_EMAIL.getTitle(),
                SendCloudTemplate.ACTIVE_EMAIL.generateContent(emailParameters)));

        return true;
    }

    @Override
    @Transactional
    public String verifyEmail(String loginName, String uuid, String ip, String platform, String deviceId) {
        if (StringUtils.isEmpty(uuid)) {
            return null;
        }
        String bindEmailKeyTemplate = "web:{loginName}:{uuid}";
        String bindEmailKey = bindEmailKeyTemplate.replace("{loginName}", loginName).replace("{uuid}", uuid);
        String bindEmailValue = redisWrapperClient.get(bindEmailKey);
        if (StringUtils.isEmpty(bindEmailValue)) {
            logger.info(bindEmailKey + "绑定邮箱失败，绑定邮箱链接已经过期!");
            return null;
        }
        String[] loginNameAndEmail = bindEmailValue.split(":");
        if (!loginName.equals(loginNameAndEmail[0])) {
            logger.info(MessageFormat.format("绑定邮箱失败，绑定邮箱链接非法! bindEmailKey={0} bindEmailValue={1}", bindEmailKey, bindEmailValue));
            return null;
        }
        String email = loginNameAndEmail[1];
        UserModel userModelEmail = userMapper.findByEmail(email);
        if (userModelEmail != null) {
            logger.error(MessageFormat.format("绑定邮箱失败，{0} 已被绑定!", email));
            return null;
        }
        userMapper.updateEmail(loginName, email);
        redisWrapperClient.del(bindEmailKey);
        //发送用户行为日志 MQ
        String mobile= Optional.ofNullable(userMapper.findByLoginName(loginName)).orElse(new UserModel()).getMobile();
        UserOpLogMessage userOpLogMessage=new UserOpLogMessage(IdGenerator.generate(),loginName,mobile,UserOpType.BIND_CHANGE_EMAIL,ip,deviceId,platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)),email != null ? "Success, Email: " + email : "Fail");
        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(userOpLogMessage));
        } catch (JsonProcessingException e) {
            logger.error("[BindEmailService] " +"verifyEmail"+ ", send UserOperateLog fail.", e);
        }

        return email;
    }

}
