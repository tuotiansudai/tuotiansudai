package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.InvestCelebrationHeroRankingMapper;
import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Component
public class InvestSuccessNationalMidAutumnMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessNationalMidAutumnMessageConsumer.class);

    @Autowired
    private InvestCelebrationHeroRankingMapper investCelebrationHeroRankingMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_NationalMidAutumn;
    }

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.day.startTime}\")}")
    private Date activityNationalDayStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.day.endTime}\")}")
    private Date activityNationalDayEndTime;

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }

        try {
            InvestSuccessMessage investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);

            UserInfo userInfo = investSuccessMessage.getUserInfo();
            InvestInfo investInfo = investSuccessMessage.getInvestInfo();
            LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();
            Date date = DateTime.now().toDate();

            if (!activityNationalDayStartTime.after(date) && !date.after(activityNationalDayEndTime) &&
                    !loanDetailInfo.getActivityType().equals("NEWBIE")
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
