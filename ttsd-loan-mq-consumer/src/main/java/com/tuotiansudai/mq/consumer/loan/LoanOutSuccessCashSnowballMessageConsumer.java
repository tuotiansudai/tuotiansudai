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
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class LoanOutSuccessCashSnowballMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessCashSnowballMessageConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    //判断是否发过
    public static final String CASH_SNOWBALL_CASH_KEY = "CASH_SNOWBALL_CASH_KEY";

    //记录用户年化投资未满1万的金额
    public static final String CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT = "CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT";

    private static final String HKEY = "{0}:{1}";

    private static final long AMOUNT = 1000000;

    private final int lifeSecond = 180 * 24 * 60 * 60;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_CashSnowball;
    }

    private Date activityCashSnowballStartTime = DateTime.parse(ETCDConfigReader.getReader().getValue("activity.cash.snowball.startTime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();;

    private Date activityCashSnowballEndTime = DateTime.parse(ETCDConfigReader.getReader().getValue("activity.cash.snowball.endTime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).minusDays(7).toDate();

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_CashSnowball receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_CashSnowball receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_CashSnowball loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_CashSnowball json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanOutInfo.getLoanId());
        LoanModel loanModel = loanMapper.findById(loanOutInfo.getLoanId());
        if (loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals("逢万返百")) {
            logger.info(MessageFormat.format("[现金滚雪球活动逢万返百标的放款MQ] LoanOutSuccess_CashSnowball send cash is executing, loanId:{0}", String.valueOf(loanOutInfo.getLoanId())));
            List<InvestAchievementView> invests = investMapper.findAmountOrderByLoanId(loanOutInfo.getLoanId(), activityCashSnowballStartTime, activityCashSnowballEndTime, null);
            for (InvestAchievementView investAchievementView : invests) {
                long annualizedAmount = investAchievementView.getAmount() * loanModel.getProductType().getDuration() / 360;
                if (!redisWrapperClient.hexists(CASH_SNOWBALL_CASH_KEY, MessageFormat.format(HKEY, String.valueOf(loanOutInfo.getLoanId()), investAchievementView.getLoginName()))) {
                    try {
                        sendCashPrize(investAchievementView.getLoginName(), annualizedAmount, loanOutInfo.getLoanId());
                    } catch (Exception e) {
                        logger.error("[逢万返百标的放款MQ] LoanOutSuccess_NationalMidAutumn user:{0}, loan:{1}, annualizedAmount:{2} is send fail.", investAchievementView.getLoginName(), loanOutInfo.getLoanId(), annualizedAmount);
                    }
                }
            }
        }
    }

    public void sendCashPrize(String loginName, long annualizedAmount, long loanId){
        logger.info("send cash snowball activity prize begin, loginName:{0}, loanId:{1}, annualizedAmount:{2}", loginName, loanId, annualizedAmount);

        if (redisWrapperClient.hexists(CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT, loginName)){
            annualizedAmount = annualizedAmount + Long.parseLong(redisWrapperClient.hget(CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT, loginName));
        }

        //记录剩余的未满1万的年化金额，下次放款使用
        redisWrapperClient.hset(CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT, loginName, String.valueOf(annualizedAmount % AMOUNT), lifeSecond);

        if (annualizedAmount < AMOUNT){
            logger.info("annualized amount is less than 10000, no prize");
            return;
        }

        long sendCashPrize = annualizedAmount / AMOUNT * 10000;
        long orderId = IdGenerator.generate();
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(orderId), String.valueOf(sendCashPrize),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVEST_RETURN_CASH_DETAIL_TEMPLATE);

        String hkey = MessageFormat.format(HKEY, String.valueOf(loanId), loginName);

        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("send cash snowball activity prize success, loginName:{}, loanId:{}, cash:{}", loginName, loanId, sendCashPrize);
                redisWrapperClient.hset(CASH_SNOWBALL_CASH_KEY, hkey, "success", lifeSecond);
                return;
            }
        } catch (Exception e) {
            logger.error("send cash snowball activity prize fail, loginName:{}, loanId:{}, cash", loginName, loanId, sendCashPrize);
        }
        redisWrapperClient.hset(CASH_SNOWBALL_CASH_KEY, hkey, "fail", lifeSecond);
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【逢万返百标的放款】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(sendCashPrize))));
        logger.info("send cash snowball activity prize end, loginName:{0}, loanId:{1}, annualizedAmount:{2}", loginName, loanId, annualizedAmount);
    }
}
