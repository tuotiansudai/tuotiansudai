package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class LoanOutSuccessDoubleElevenMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessDoubleElevenMessageConsumer.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.startTime}\")}")
    private Date activityDoubleElevenStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.endTime}\")}")
    private Date activityDoubleElevenEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_DoubleEleven;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[双11剁手活动标的放款MQ] LoanOutSuccess_DoubleEleven receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[双11剁手活动标的放款MQ] LoanOutSuccess_DoubleEleven receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[双11剁手活动标的放款MQ] LoanOutSuccess_DoubleEleven loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[双11剁手活动标的放款MQ] LoanOutSuccess_DoubleEleven json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanOutInfo.getLoanId());
        LoanModel loanModel = loanMapper.findById(loanOutInfo.getLoanId());

        if (loanModel.getId() !=1 && !loanModel.getActivityType().equals(ActivityType.NEWBIE) && (Strings.isNullOrEmpty(loanDetailsModel.getActivityDesc()) || !loanDetailsModel.getActivityDesc().equals("0元购"))) {
            logger.info(MessageFormat.format("[双11剁手活动标的放款MQ] LoanOutSuccess_DoubleEleven send experience is executing , (loanId : {0}) ", String.valueOf(loanOutInfo.getLoanId())));
            List<InvestModel> invests = investMapper.findSuccessDoubleElevenActivityByTime(loanOutInfo.getLoanId(), activityDoubleElevenStartTime, activityDoubleElevenEndTime);
            int count = 1;
            for(InvestModel investModel: invests){
                if(count %2 == 1){
                    long experienceAmount = new BigDecimal(investModel.getAmount() * 1.1).longValue();
                    try {
                        grantExperience(investModel.getLoginName(), experienceAmount);
                    }
                    catch(Exception e){
                        logger.error("[双11剁手活动标的放款MQ] LoanOutSuccess_DoubleEleven 用户:{0}, 标的:{1}, 体验金金额:{2} is send fail.", investModel.getLoginName(), loanOutInfo.getLoanId(), experienceAmount);
                    }
                }
                count++;
            }
        }

    }

    private void grantExperience(String loginName, long experienceAmount) {
        logger.info("send double eleven activity of experience prize begin, loginName:{},  experienceAmount:{}", loginName, experienceAmount);

        mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                new ExperienceAssigningMessage(loginName, experienceAmount, ExperienceBillOperationType.IN, ExperienceBillBusinessType.DOUBLE_ELEVEN));

        logger.info("send double eleven activity of experience prize prize end, loginName:{}, experienceAmount:{}", loginName, experienceAmount);
    }
}
