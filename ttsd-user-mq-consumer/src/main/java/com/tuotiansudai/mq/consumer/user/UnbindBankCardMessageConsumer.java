package com.tuotiansudai.mq.consumer.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserBankCardStatus;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UnbindBankCardMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(UnbindBankCardMessageConsumer.class);

    private static List<String> JSON_KEYS = Lists.newArrayList("loginName", "mobile", "bankUserName", "bankAccountNo", "bank", "bankCode", "cardNumber", "bankOrderNo", "bankOrderDate");

    private final UserBankCardMapper userBankCardMapper;

    @Autowired
    public UnbindBankCardMessageConsumer(UserBankCardMapper userBankCardMapper) {
        this.userBankCardMapper = userBankCardMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.UnbindBankCard_Success;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        try {
            Map<String, String> map = JsonConverter.readValue(message, new TypeReference<HashMap<String, String>>() {
            });

            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                UserBankCardModel model = this.userBankCardMapper.findByLoginName(map.get("loginName"));
                if (model != null && map.get("cardNumber").equalsIgnoreCase(model.getCardNumber())) {
                    model.setStatus(UserBankCardStatus.UNBOUND);
                    userBankCardMapper.updateStatus(model.getId(), UserBankCardStatus.UNBOUND);
                    return;
                }
                logger.error("[MQ] bank card is not exited, message: {}", message);
            } else {
                logger.error("[MQ] message is invalid {}", message);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}