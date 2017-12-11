package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.CashSnowballActivityMapper;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
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
public class InvestSuccessCashSnowballMessageConsumer implements MessageConsumer {

    public static Logger logger = LoggerFactory.getLogger(InvestSuccessCashSnowballMessageConsumer.class);

    @Autowired
    private CashSnowballActivityMapper cashSnowballActivityMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.cash.snowball.startTime}\")}")
    private Date activityCashSnowballStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.cash.snowball.endTime}\")}")
    private Date activityCashSnowballEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_CashSnowball;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }
        Date date = new Date();
        if (date.before(activityCashSnowballStartTime) || date.after(activityCashSnowballEndTime)) {
            logger.info("[MQ] InvestSuccess_CashSnowball, cash snowball activity not in the activity time range");
            return;
        }

        InvestSuccessMessage investSuccessMessage;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            logger.error("[MQ] InvestSuccess_CashSnowball, json convert InvestSuccessMessage is fail, message:{}", message);
            return;
        }

        UserInfo userInfo = investSuccessMessage.getUserInfo();
        InvestInfo investInfo = investSuccessMessage.getInvestInfo();
        LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();

        if (!loanDetailInfo.getActivityType().equals("NEWBIE")
                && !investInfo.getTransferStatus().equals("SUCCESS")
                && investInfo.getStatus().equals("SUCCESS")
                && loanDetailInfo.getActivityDesc().equals("逢万返百")) {

            long annualizedAmount = calculatorsAnnualizedAmount(investInfo.getAmount(), loanDetailInfo.getDuration());

            CashSnowballActivityModel cashSnowballActivityModel = cashSnowballActivityMapper.findByLoginName(userInfo.getLoginName());
            if (cashSnowballActivityModel == null) {
                cashSnowballActivityMapper.create(new CashSnowballActivityModel(
                        investInfo.getInvestId(),
                        userInfo.getLoginName(),
                        userInfo.getUserName(),
                        userInfo.getMobile(),
                        annualizedAmount,
                        annualizedAmount / 100));
            } else {
                annualizedAmount = cashSnowballActivityModel.getAnnualizedAmount() + annualizedAmount;
                cashSnowballActivityModel.setAnnualizedAmount(annualizedAmount);
                cashSnowballActivityModel.setCashAmount(annualizedAmount / 100);
                cashSnowballActivityMapper.update(cashSnowballActivityModel);
            }
        }
    }

    public long calculatorsAnnualizedAmount(long investAmount, int duration) {
        switch (duration) {
            case 90:
                return investAmount / 4;
            case 180:
                return investAmount / 2;
            case 360:
                return investAmount;
        }
        return 0l;
    }
}
