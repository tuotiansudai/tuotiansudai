package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.AnnualizedInvestUtil;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class InvestSuccessActivityInvestMessageConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(InvestSuccessActivityInvestMessageConsumer.class);

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.startTime}\")}")
    private Date activitySuperScholarStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.endTime}\")}")
    private Date activitySuperScholarEndTime;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String REFERRER_ACTIVITY_SUPER_SCHOLAR_REGISTER = "REFERRER_ACTIVITY_SUPER_SCHOLAR_REGISTER:{0}:{1}";

    private final String REFERRER_ACTIVITY_SUPER_SCHOLAR_INVEST = "REFERRER_ACTIVITY_SUPER_SCHOLAR_INVEST:{0}:{1}";

    private final int seconds = 60 * 24 * 60 * 60;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ActivityInvest;
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
            Date date = DateTime.now().toDate();
            if (!activitySuperScholarStartTime.after(date) && !date.after(activitySuperScholarEndTime)
                    && !loanDetailInfo.getActivityType().equals("NEWBIE")
                    && !investInfo.getTransferStatus().equals("SUCCESS")
                    && investInfo.getStatus().equals("SUCCESS")) {

                long annualizedAmount = AnnualizedInvestUtil.annualizedInvestAmount(investInfo.getAmount(), loanDetailInfo.getDuration());
                activityInvestMapper.create(new ActivityInvestModel(loanDetailInfo.getLoanId(),
                        investInfo.getInvestId(),
                        userInfo.getLoginName(),
                        userInfo.getUserName(),
                        userInfo.getMobile(),
                        investInfo.getAmount(),
                        annualizedAmount,
                        ActivityCategory.SUPER_SCHOLAR_ACTIVITY.name()));

                referrerSuperScholarActivityInvest(userInfo.getLoginName());
            }

        } catch (Exception e) {
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }


    private void referrerSuperScholarActivityInvest(String loginName){
        long sumAmount = activityInvestMapper.sumInvestAmountByActivityLoginNameAndTime(loginName,
                ActivityCategory.SCHOOL_SEASON_ACTIVITY.name(),
                DateTime.now().withTimeAtStartOfDay().toDate(),
                new Date());

        String currentDate = DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTime.now());
        String referrerKey = MessageFormat.format(REFERRER_ACTIVITY_SUPER_SCHOLAR_REGISTER, currentDate, loginName);
        if (sumAmount >= 100000L && redisWrapperClient.exists(referrerKey)){
            String referrer = redisWrapperClient.get(referrerKey);
            redisWrapperClient.setex(MessageFormat.format(REFERRER_ACTIVITY_SUPER_SCHOLAR_INVEST, currentDate, referrer), seconds, "SUCCESS");
        }
    }
}
