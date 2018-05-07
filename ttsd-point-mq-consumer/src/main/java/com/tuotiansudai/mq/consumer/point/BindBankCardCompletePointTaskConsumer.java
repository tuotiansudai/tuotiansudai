package com.tuotiansudai.mq.consumer.point;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointTaskService;
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
public class BindBankCardCompletePointTaskConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BindBankCardCompletePointTaskConsumer.class);

    private static List<String> JSON_KEYS = Lists.newArrayList("loginName", "mobile", "bankUserName", "bankAccountNo", "bank", "bankCode", "cardNumber", "bankOrderNo", "bankOrderDate");

    private final PointTaskService pointTaskService;

    @Autowired
    public BindBankCardCompletePointTaskConsumer(PointTaskService pointTaskService) {
        this.pointTaskService = pointTaskService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.BindBankCard_CompletePointTask;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);

        try {
            Map<String, String> map = JsonConverter.readValue(message, new TypeReference<HashMap<String, String>>() {
            });

            if (Sets.difference(map.keySet(), Sets.newHashSet(JSON_KEYS)).isEmpty()) {
                this.pointTaskService.completeNewbieTask(PointTask.BIND_BANK_CARD, message);
            } else {
                logger.error("[MQ] message is invalid {}", message);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
