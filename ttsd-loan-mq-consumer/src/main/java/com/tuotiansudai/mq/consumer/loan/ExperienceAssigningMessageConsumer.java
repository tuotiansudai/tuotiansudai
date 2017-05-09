package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.ExperienceBillService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;


@Component
public class ExperienceAssigningMessageConsumer implements MessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(ExperienceAssigningMessageConsumer.class);

    private static final String NEWMAN_TYRANT_GRANTED_LIST = "NEWMAN_TYRANT_GRANTED_LIST";

    private final int lifeSecond = 10378000;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private ExperienceBillService experienceBillService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.ExperienceAssigning;
    }

    @Override
    public void consume(String message) {
        logger.info("[发放体验金MQ] receive message: {}: {}.", this.queue(), message);
        ExperienceAssigningMessage experienceAssigningMessage;
        try {
            experienceAssigningMessage = JsonConverter.readValue(message, ExperienceAssigningMessage.class);
        } catch (IOException e) {
            logger.error("[发放体验金MQ] json convert experienceAssigningMessage is fail, message:{}", message);
            throw new RuntimeException(e);
        }

        if (experienceAssigningMessage == null) {
            logger.error("[发放体验金MQ] ExperienceAssigningMessage is empty.");
            return;
        }

        if (experienceAssigningMessage.getExperienceBillBusinessType().equals(ExperienceBillBusinessType.NEWMAN_TYRANT)) {
            this.newmanTyrantAssignExperience(experienceAssigningMessage);
            return;
        }

        logger.info("[发放体验金MQ] ready to consume message: tyrant assign experience.");
        try {
            experienceBillService.updateUserExperienceBalanceByLoginName(experienceAssigningMessage.getExperienceAmount(), experienceAssigningMessage.getLoginName(),
                    experienceAssigningMessage.getExperienceBillOperationType(), experienceAssigningMessage.getExperienceBillBusinessType(), experienceAssigningMessage.getNote());
        } catch (Exception e) {
            logger.error(MessageFormat.format("[发放体验金MQ] {0} grant experience fail {1}, errorMessage:{2}",
                    experienceAssigningMessage.getLoginName(),
                    DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd"),
                    e.getLocalizedMessage()), e);
        }
    }

    private void newmanTyrantAssignExperience(ExperienceAssigningMessage experienceAssigningMessage) {
        logger.info("[新贵富豪争霸活动发放体验金MQ] ready to consume message: newman tyrant assign experience.");
        if (redisWrapperClient.hexists(NEWMAN_TYRANT_GRANTED_LIST, DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd"))
                && redisWrapperClient.hget(NEWMAN_TYRANT_GRANTED_LIST, DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd")).contains(experienceAssigningMessage.getLoginName())) {
            logger.info(MessageFormat.format("loginName:{0} had granted experience {1}", experienceAssigningMessage.getLoginName(), DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd")));
            return;
        }
        try {
            experienceBillService.updateUserExperienceBalanceByLoginName(588800, experienceAssigningMessage.getLoginName(), ExperienceBillOperationType.IN, ExperienceBillBusinessType.NEWMAN_TYRANT,
                    MessageFormat.format(ExperienceBillBusinessType.NEWMAN_TYRANT.getContentTemplate(),
                            AmountConverter.convertCentToString(588800),
                            experienceAssigningMessage.getCurrentDate()));

            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.NEWMAN_TYRANT,
                    Lists.newArrayList(experienceAssigningMessage.getLoginName()),
                    MessageEventType.NEWMAN_TYRANT.getTitleTemplate(),
                    MessageFormat.format(MessageEventType.NEWMAN_TYRANT.getContentTemplate(), org.apache.commons.lang3.time.DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd")),
                    null));
            logger.info(String.format("[新贵富豪争霸活动发放体验金MQ %s] grant %s experience end ...",
                    org.apache.commons.lang3.time.DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd"),
                    experienceAssigningMessage.getLoginName()));

            String loginNameList = redisWrapperClient.hget(NEWMAN_TYRANT_GRANTED_LIST, DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd"));
            redisWrapperClient.hset(NEWMAN_TYRANT_GRANTED_LIST,
                    DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(), "yyyy-MM-dd"),
                    MessageFormat.format("{0},{1}", Strings.isNullOrEmpty(loginNameList) ? "" : loginNameList, experienceAssigningMessage.getLoginName()), lifeSecond);
        } catch (Exception e) {
            logger.error("[新贵富豪争霸活动发放体验金MQ] {0} grant experience fail {1}, errorMessage:{2}",
                    experienceAssigningMessage.getLoginName(), DateFormatUtils.format(experienceAssigningMessage.getCurrentDate(),
                            "yyyy-MM-dd"), e.getLocalizedMessage());
        }
    }
}
