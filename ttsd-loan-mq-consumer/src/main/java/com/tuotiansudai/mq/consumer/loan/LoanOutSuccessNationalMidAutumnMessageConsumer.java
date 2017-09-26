package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
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
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class LoanOutSuccessNationalMidAutumnMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessNationalMidAutumnMessageConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.midAutumn.startTime}\")}")
    private Date activityNationalMidAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.midAutumn.endTime}\")}")
    private Date activityNationalMidAutumnEndTime;

    //判断是否发过
    public static final String NATIONAL_MID_AUTUMN_CASH_KEY = "NATIONAL_MID_AUTUMN_CASH_KEY";

    //已发的现金总额
    public static final String NATIONAL_MID_AUTUMN_SUM_CASH_KEY = "NATIONAL_MID_AUTUMN_SUM_CASH_KEY";

    private final int lifeSecond = 180 * 24 * 60 * 60;

    private static final String HKEY = "{0}:{1}";

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_NationalMidAutumn;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanOutInfo.getLoanId());

        if (loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals("逢万返百")) {
            logger.info(MessageFormat.format("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn send cash is executing , (loanId : {0}) ", String.valueOf(loanOutInfo.getLoanId())));
            List<InvestAchievementView> invests = investMapper.findAmountOrderByLoanId(loanOutInfo.getLoanId(), activityNationalMidAutumnStartTime, activityNationalMidAutumnEndTime, null);
            for (InvestAchievementView investAchievementView : invests) {
                if (!redisWrapperClient.hexists(NATIONAL_MID_AUTUMN_CASH_KEY, MessageFormat.format(HKEY, String.valueOf(loanOutInfo.getLoanId()), investAchievementView.getLoginName()) + investAchievementView.getLoginName())) {
                    try {
                        sendCashPrize(investAchievementView.getLoginName(), investAchievementView.getAmount(), loanOutInfo.getLoanId());
                    } catch (Exception e) {
                        logger.error("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn 用户:{0}, 标的:{1}, 投资金额:{2} is send fail.", investAchievementView.getAmount(), loanOutInfo.getLoanId(), investAchievementView.getAmount());
                    }
                }
            }
        }

    }

    private void sendCashPrize(String loginName, long investAmount, long loanId) {
        logger.info("send has_thousand_sent_hundred invest cash prize begin, loginName:{}, loanId:{}, investAmount:{}", loginName, loanId, investAmount);

        if (investAmount < 1000000) {
            logger.info("invest amount is less than 10000, no prize.");
            return;
        }

        long sendPrizeAmount = 0;
        if (redisWrapperClient.hexists(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, loginName)) {
            sendPrizeAmount = Long.parseLong(redisWrapperClient.hget(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, loginName));
        }

        if (sendPrizeAmount >= 1000000) {
            logger.info("send cash is more than 10000, no prize.");
            return;
        }

        long investAmountCash = (investAmount / 1000000) * 10000;
        long prizeAmount = investAmountCash > (1000000 - sendPrizeAmount) ? (1000000 - sendPrizeAmount) : investAmountCash;

        long orderId = IdGenerator.generate();
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(orderId), String.valueOf(prizeAmount),
                UserBillBusinessType.NATIONAL_DAY_INVEST, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVEST_RETURN_CASH_DETAIL_TEMPLATE);

        String hkey = MessageFormat.format(HKEY, String.valueOf(loanId), loginName);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("send has_thousand_sent_hundred invest cash prize success, loginName:{}, loanId:{}, cash:{}", loginName, loanId, prizeAmount);
                redisWrapperClient.hset(NATIONAL_MID_AUTUMN_CASH_KEY, hkey, "success", lifeSecond);
                redisWrapperClient.hset(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, loginName, String.valueOf(prizeAmount + sendPrizeAmount), lifeSecond);
                return;
            }
        } catch (Exception e) {
            logger.error("send has_thousand_sent_hundred invest cash prize fail, loginName:{}, loanId:{}, cash", loginName, loanId, prizeAmount);
        }

        redisWrapperClient.hset(NATIONAL_MID_AUTUMN_CASH_KEY, hkey, "fail", lifeSecond);
        redisWrapperClient.hset(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, loginName, String.valueOf(prizeAmount + sendPrizeAmount), lifeSecond);

        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【逢万返百标的放款】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(prizeAmount))));

        logger.info("send has_thousand_sent_hundred invest cash prize end, loginName:{}, loanId:{}, investAmount:{}", loginName, loanId, investAmount);
    }
}
