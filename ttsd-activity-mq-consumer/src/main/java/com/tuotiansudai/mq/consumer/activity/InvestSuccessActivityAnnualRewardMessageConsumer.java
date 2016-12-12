package com.tuotiansudai.mq.consumer.activity;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class InvestSuccessActivityAnnualRewardMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessActivityAnnualRewardMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;

    @Autowired
    private NotWorkMapper notWorkMapper;

    @Value("#{'${activity.annual.period}'.split('\\~')}")
    private List<String> annualTime = Lists.newArrayList();

    final private static String LOAN_ANNUAL_ACTIVITY_DESCRIPTION = "活动专享";

    final static private long INVEST_20_RED_ENVELOPE_LIMIT = 500000L;

    final static private long INVEST_800_RED_ENVELOPE_LIMIT = 30000000;

    final static private long INTEREST_COUPON_OF_20_COUPON_ID = 330;

    final static private long INTEREST_COUPON_OF_800_COUPON_ID = 331;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ActivityReward;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            this.assignActivityAnnualInvestReward(message);
        }
    }

    private void assignActivityAnnualInvestReward(String message){
        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Date nowDate = DateTime.now().toDate();
        InvestInfo investInfo = investSuccessMessage.getInvestInfo();
        LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();

        logger.info("[MQ] ready to consume activity annual message: invest reward.");
        Date startTime = DateTime.parse(annualTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(annualTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        if ((startTime.before(nowDate) && endTime.after(nowDate))
                && loanDetailInfo.isActivity() && loanDetailInfo.getActivityDesc().equals(LOAN_ANNUAL_ACTIVITY_DESCRIPTION)
                && (!investInfo.getTransferStatus().equals("SUCCESS") && investInfo.getStatus().equals("SUCCESS"))){

            NotWorkModel activityInvestRecord = notWorkMapper.findByLoginName(investInfo.getLoginName(), ActivityCategory.ANNUAL_ACTIVITY);
            long originalInvestAmount = 0l;
            if(null != activityInvestRecord){
                originalInvestAmount = activityInvestRecord.getInvestAmount();
                activityInvestRecord.setInvestAmount(activityInvestRecord.getInvestAmount() + investInfo.getAmount());
                notWorkMapper.update(activityInvestRecord);
            }else{
                activityInvestRecord = new NotWorkModel(investInfo.getLoginName(), null, null, false, ActivityCategory.ANNUAL_ACTIVITY);
                activityInvestRecord.setInvestAmount(investInfo.getAmount());
                notWorkMapper.create(activityInvestRecord);
            }

            if(originalInvestAmount < INVEST_20_RED_ENVELOPE_LIMIT && activityInvestRecord.getInvestAmount() >= INVEST_20_RED_ENVELOPE_LIMIT){
                mqClient.sendMessage(MessageQueue.CouponAssigning, investSuccessMessage.getInvestInfo().getLoginName() + ":" + INTEREST_COUPON_OF_20_COUPON_ID);
            }

            if(originalInvestAmount < INVEST_800_RED_ENVELOPE_LIMIT && activityInvestRecord.getInvestAmount() >= INVEST_800_RED_ENVELOPE_LIMIT){
                mqClient.sendMessage(MessageQueue.CouponAssigning, investSuccessMessage.getInvestInfo().getLoginName() + ":" + INTEREST_COUPON_OF_800_COUPON_ID);
            }
        }
    }
}
