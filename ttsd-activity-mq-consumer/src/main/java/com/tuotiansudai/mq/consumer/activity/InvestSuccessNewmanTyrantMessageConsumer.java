package com.tuotiansudai.mq.consumer.activity;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.InvestNewmanTyrantMapper;
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
public class InvestSuccessNewmanTyrantMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessNewmanTyrantMessageConsumer.class);

    @Value("#{'${activity.newmanTyrant.activity.period}'.split('\\~')}")
    private List<String> newmanTyrantActivityPeriod = Lists.newArrayList();
    @Autowired
    private InvestNewmanTyrantMapper investNewmanTyrantMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_InvestNewmanTyrant;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        InvestSuccessNewmanTyrantMessage investSuccessNewmanTyrantMessage;
        try {
            investSuccessNewmanTyrantMessage = JsonConverter.readValue(message, InvestSuccessNewmanTyrantMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserInfoActivity userInfo = investSuccessNewmanTyrantMessage.getUserInfoActivity();
        InvestInfo investInfo = investSuccessNewmanTyrantMessage.getInvestInfo();
        if (isActivityPeriod()
                && !investInfo.getTransferStatus().equals("SUCCESS")
                && investInfo.getStatus().equals("SUCCESS")) {
            InvestNewmanTyrantModel investNewmanTyrantModel = new InvestNewmanTyrantModel(investInfo.getInvestId(),
                    userInfo.getLoginName(),
                    userInfo.getUserName(),
                    userInfo.getMobile(),
                    investInfo.getAmount(),
                    isNewman(userInfo.getRegisterTime())

            );
            investNewmanTyrantMapper.create(investNewmanTyrantModel);
        }


    }

    private boolean isActivityPeriod() {
        Date startTime = DateTime.parse(newmanTyrantActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(newmanTyrantActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date nowDate = DateTime.now().toDate();
        return startTime.compareTo(nowDate) <= 0 && endTime.compareTo(nowDate) >=0;

    }
    private boolean isNewman(Date registerTime){
        Date startTime = DateTime.parse(newmanTyrantActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(newmanTyrantActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date newManStartTime = new DateTime(startTime).minusDays(30).toDate();
        return newManStartTime.compareTo(registerTime) <= 0 && endTime.compareTo(registerTime) >=0;
    }


}
