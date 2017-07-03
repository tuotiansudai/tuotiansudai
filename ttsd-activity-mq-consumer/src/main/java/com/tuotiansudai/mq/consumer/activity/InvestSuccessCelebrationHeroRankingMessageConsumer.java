package com.tuotiansudai.mq.consumer.activity;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.InvestCelebrationHeroRankingMapper;
import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class InvestSuccessCelebrationHeroRankingMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessCelebrationHeroRankingMessageConsumer.class);

    @Value("#{'${activity.celebrationHeroRanking.activity.period}'.split('\\~')}")
    private List<String> celebrationHeroRankingActivityPeriod = Lists.newArrayList();
    @Autowired
    private InvestCelebrationHeroRankingMapper investCelebrationHeroRankingMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_InvestHeroRanking;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        InvestSuccessCelebrationHeroRankingMessage investSuccessCelebrationHeroRankingMessage;
        try {
            investSuccessCelebrationHeroRankingMessage = JsonConverter.readValue(message, InvestSuccessCelebrationHeroRankingMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserInfoActivity userInfo = investSuccessCelebrationHeroRankingMessage.getUserInfoActivity();
        InvestInfo investInfo = investSuccessCelebrationHeroRankingMessage.getInvestInfo();
        if (isActivityPeriod()
                && !investInfo.getTransferStatus().equals("SUCCESS")
                && investInfo.getStatus().equals("SUCCESS")) {
            InvestNewmanTyrantModel investNewmanTyrantModel = new InvestNewmanTyrantModel(investInfo.getInvestId(),
                    userInfo.getLoginName(),
                    userInfo.getUserName(),
                    userInfo.getMobile(),
                    investInfo.getAmount(),
                    false
            );
            investCelebrationHeroRankingMapper.create(investNewmanTyrantModel);
        }


    }

    private boolean isActivityPeriod() {
        Date startTime = DateTime.parse(celebrationHeroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(celebrationHeroRankingActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date nowDate = DateTime.now().toDate();
        return startTime.compareTo(nowDate) <= 0 && endTime.compareTo(nowDate) >=0;

    }



}
