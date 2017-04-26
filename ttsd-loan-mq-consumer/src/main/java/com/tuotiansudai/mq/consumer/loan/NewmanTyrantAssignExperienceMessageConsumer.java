package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.message.NewmanTyrantMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.loan.service.NewmanTyrantAssignExperienceService;
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
public class NewmanTyrantAssignExperienceMessageConsumer implements MessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(NewmanTyrantAssignExperienceMessageConsumer.class);

    private static final String NEWMAN_TYRANT_GRANTED_LIST = "NEWMAN_TYRANT_GRANTED_LIST";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private NewmanTyrantAssignExperienceService newmanTyrantAssignExperienceService;

    private int lifeSecond = 10378000;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestNewmanTyrant_AssignExperience;
    }

    @Override
    public void consume(String message) {
        logger.info("[新贵富豪争霸活动发放体验金MQ] receive message: {}: {}.", this.queue(), message);
        NewmanTyrantMessage newmanTyrantMessage;
        try {
            newmanTyrantMessage = JsonConverter.readValue(message, NewmanTyrantMessage.class);
        } catch (IOException e) {
            logger.error("[新贵富豪争霸活动发放体验金MQ] json convert investSuccessNewmanTyrantMessage is fail, message:{}", message);
            throw new RuntimeException(e);
        }
        logger.info("[新贵富豪争霸活动发放体验金MQ] ready to consume message: newman tyrant assign experience.");
        if (redisWrapperClient.hexists(NEWMAN_TYRANT_GRANTED_LIST, DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd"))
                && redisWrapperClient.hget(NEWMAN_TYRANT_GRANTED_LIST, DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd")).indexOf(newmanTyrantMessage.getLoginName()) > -1) {
            logger.info(MessageFormat.format("loginName:{0} had granted experience {1}", newmanTyrantMessage.getLoginName(), DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd")));
            return;
        }
        try{

            newmanTyrantAssignExperienceService.grantExperience(newmanTyrantMessage);

            String loginNameList = redisWrapperClient.hget(NEWMAN_TYRANT_GRANTED_LIST, DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd"));
            redisWrapperClient.hset(NEWMAN_TYRANT_GRANTED_LIST,
                    DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd"),
                    MessageFormat.format("{0},{1}", Strings.isNullOrEmpty(loginNameList)?"":loginNameList, newmanTyrantMessage.getLoginName()), lifeSecond);
        }catch (Exception e){
            logger.error("[新贵富豪争霸活动发放体验金MQ] {0} grant experience fail {1}, errorMessage:{2}",
                    newmanTyrantMessage.getLoginName(),DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(),
                            "yyyy-MM-dd"), e.getLocalizedMessage());
            return;
        }

        logger.info("[新贵富豪争霸活动发放体验金MQ] newman tyrant assign experience success.");


    }


}
