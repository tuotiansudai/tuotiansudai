package com.tuotiansudai.mq.consumer.user;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.enums.WeChatMessageType;
import com.tuotiansudai.message.WeChatBoundMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.WeChatClient;
import com.tuotiansudai.util.MobileEncoder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Component
public class WeChatBoundMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(WeChatBoundMessageConsumer.class);

    private final WeChatClient weChatClient = WeChatClient.getClient();

    private final UserMapper userMapper;

    private final WeChatUserMapper weChatUserMapper;

    @Autowired
    public WeChatBoundMessageConsumer(UserMapper userMapper, WeChatUserMapper weChatUserMapper) {
        this.userMapper = userMapper;
        this.weChatUserMapper = weChatUserMapper;
    }


    @Override
    public MessageQueue queue() {
        return MessageQueue.WeChatBoundNotify;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ WeChatBoundNotify] receive message: {}.", message);

        if (Strings.isNullOrEmpty(message)) {
            return;
        }

        try {
            WeChatBoundMessage weChatBoundMessage = JsonConverter.readValue(message, WeChatBoundMessage.class);
            this.bind(weChatBoundMessage.getMobile(), weChatBoundMessage.getOpenid());
        } catch (IOException e) {
            logger.error("[MQ WeChatBoundNotify] message is invalid receive message: {}.", message);
        }

        logger.info("[MQ WeChatBoundNotify] consume message success: {}.", message);
    }

    private void bind(String mobile, String openid) {
        UserModel userModel = userMapper.findByMobile(mobile);
        WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openid);

        if (userModel == null || weChatUserModel == null) {
            return;
        }

        weChatUserMapper.findByLoginName(userModel.getLoginName())
                .stream()
                .filter(boundUser -> !boundUser.getOpenid().equals(openid) && boundUser.isBound())
                .forEach(boundUser -> {
                    boundUser.setBound(false);
                    weChatUserMapper.update(boundUser);
                    weChatClient.sendTemplateMessage(WeChatMessageType.BOUND_TO_OTHER_USER, Maps.newHashMap(ImmutableMap.<String, String>builder()
                            .put("openid", boundUser.getOpenid())
                            .put("first", "您的拓天速贷账号已被其他微信号绑定，请知悉")
                            .put("keyword1", MobileEncoder.encode(mobile))
                            .put("keyword2", new DateTime().toString("yyyy-MM-dd HH:mm"))
                            .put("remark", "如非您本人操作，请及时联系客服：400-169-1188（客服时间：工作日9:00-20:00）。")
                            .build()));
                    logger.info("[MQ WeChatBoundNotify] wechat unbound previous use successfully. user: {}, openid: {}, previous user: {}", userModel.getLoginName(), openid, boundUser.getLoginName());
                });


        weChatUserModel.setLoginName(userModel.getLoginName());
        weChatUserModel.setBound(true);
        weChatUserModel.setLatestLoginTime(new Date());

        weChatUserMapper.update(weChatUserModel);
    }
}