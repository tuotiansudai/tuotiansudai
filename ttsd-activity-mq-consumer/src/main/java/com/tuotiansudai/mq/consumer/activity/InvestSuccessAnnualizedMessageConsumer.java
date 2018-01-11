package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestAnnualizedMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualized;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualizedModel;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.AnnualizedInvestUtil;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InvestSuccessAnnualizedMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(InvestSuccessAnnualizedMessageConsumer.class);

    @Autowired
    private ActivityInvestAnnualizedMapper activityInvestAnnualizedMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ActivityAnnualized;
    }

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

            ActivityInvestAnnualized activityInvestAnnualized = ActivityInvestAnnualized.getActivityByDesc(loanDetailInfo.getActivityDesc());
            if (activityInvestAnnualized != null) {
                ActivityInvestAnnualizedModel activityInvestAnnualizedModel = activityInvestAnnualizedMapper.findByActivityAndLoginName(activityInvestAnnualized, userInfo.getLoginName());
                if (activityInvestAnnualizedModel == null) {
                    activityInvestAnnualizedMapper.create(
                            new ActivityInvestAnnualizedModel(
                                    userInfo.getUserName(),
                                    userInfo.getLoginName(),
                                    userInfo.getMobile(),
                                    investInfo.getAmount(),
                                    AnnualizedInvestUtil.annualizedInvestAmount(investInfo.getAmount(), loanDetailInfo.getDuration()),
                                    activityInvestAnnualized,
                                    loanDetailInfo.getActivityDesc()));
                } else {
                    activityInvestAnnualizedModel.setSumInvestAmount(activityInvestAnnualizedModel.getSumInvestAmount() + investInfo.getAmount());
                    activityInvestAnnualizedModel.setSumAnnualizedAmount(activityInvestAnnualizedModel.getSumAnnualizedAmount() + AnnualizedInvestUtil.annualizedInvestAmount(investInfo.getAmount(), loanDetailInfo.getDuration()));
                    activityInvestAnnualizedMapper.update(activityInvestAnnualizedModel);
                }
            }

        } catch (Exception e) {
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }
}
