package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SchoolExclusiveMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SchoolExclusiveModel;
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
public class InvestSuccessSchoolSeasonMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessSchoolSeasonMessageConsumer.class);

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private SchoolExclusiveMapper schoolExclusiveMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.startTime}\")}")
    private Date activitySchoolSeasonStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.endTime}\")}")
    private Date activitySchoolSeasonEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_SchoolSeason;
    }

    @Override
    @Transactional
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
            if (duringActivities()
                    && loanDetailInfo.isActivity()
                    && loanDetailInfo.getActivityDesc().equals("早鸟专享")){
                schoolExclusive(userInfo, investInfo);
            }

            if (duringActivities()
                    && !loanDetailInfo.getActivityType().equals("NEWBIE")
                    && !investInfo.getTransferStatus().equals("SUCCESS")
                    && investInfo.getStatus().equals("SUCCESS")) {
                activityInvestMapper.create(new ActivityInvestModel(investInfo.getInvestId(),
                        userInfo.getLoginName(),
                        userInfo.getUserName(),
                        userInfo.getMobile(),
                        investInfo.getAmount(),
                        ActivityCategory.SCHOOL_SEASON_ACTIVITY.name()));
            }

        } catch (IOException e) {
            logger.error("[MQ] parse message failed: {}: {}.", this.queue(), message);
        }

    }

    public void schoolExclusive(UserInfo userInfo, InvestInfo investInfo){
        int topThreeCount = schoolExclusiveMapper.sumTopThreeIsTrue(investInfo.getInvestId());
        SchoolExclusiveModel schoolExclusiveModel = schoolExclusiveMapper.findSchoolExclusiveModel(investInfo.getInvestId(), userInfo.getLoginName());
        long sumAmount = schoolExclusiveModel==null? investInfo.getAmount():investInfo.getAmount()+schoolExclusiveModel.getSumAmount();
        boolean isTopThree = false;
        if (topThreeCount<3 && sumAmount>= 5000000l){
            isTopThree=true;
        }
        if (schoolExclusiveModel == null){
            schoolExclusiveMapper.create(new SchoolExclusiveModel(investInfo.getInvestId(),
                    userInfo.getLoginName(),
                    investInfo.getAmount(),
                    isTopThree,
                    DateTime.now()));

        }else{
            schoolExclusiveModel.setSumAmount(sumAmount);
            schoolExclusiveModel.setTopThree(isTopThree);
            schoolExclusiveMapper.update(schoolExclusiveModel);
        }

    }

    private boolean duringActivities() {
        return activitySchoolSeasonStartTime.compareTo(new Date()) <= 0 && activitySchoolSeasonEndTime.compareTo(new Date()) >=0;
    }
}
