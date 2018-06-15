package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessSendCashRewardMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessSendCashRewardMessageConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private static final String LOAN_OUT_SUCCESS_SEND_REWARD_KEY = "LOAN_OUT_SUCCESS_SEND_REWARD_KEY";

    private static final String HKEY = "{0}:{1}";

    private final int lifeSecond = 180 * 24 * 60 * 60;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_SendCashReward;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_SendCashReward receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_SendCashReward receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_SendCashReward loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_SendCashReward json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        LoanModel loanModel = loanMapper.findById(loanOutInfo.getLoanId());
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanOutInfo.getLoanId());

        if (loanDetailsModel.getGrantReward() && loanDetailsModel.getRewardRate() > 0) {
            List<InvestAchievementView> invests = investMapper.findAmountOrderByLoanId(loanOutInfo.getLoanId(), null, null, null);
            for (InvestAchievementView investAchievementView : invests) {
                long cashAmount = (long) (investAchievementView.getAmount() * loanModel.getProductType().getDuration() / 360 * loanDetailsModel.getRewardRate());
                if (!redisWrapperClient.hexists(LOAN_OUT_SUCCESS_SEND_REWARD_KEY, MessageFormat.format(HKEY, String.valueOf(loanOutInfo.getLoanId()), investAchievementView.getLoginName()))) {
                    try {
                        sendCashPrize(investAchievementView.getLoginName(), cashAmount, loanOutInfo.getLoanId());
                    } catch (Exception e) {
                        logger.error("[loan out send cash reward MQ] LoanOutSuccess_SendCashReward user:{}, loan:{}, cash:{} is send fail.", investAchievementView.getLoginName(), loanOutInfo.getLoanId(), cashAmount);
                    }
                }
            }
        }
    }

    private void sendCashPrize(String loginName, long cashAmount, long loanId) {
        logger.info("loan out send cash reward begin, loginName:{}, loanId:{}, sendCash:{}", loginName, loanId, cashAmount);

        if (cashAmount <= 0) {
            logger.error("[loan out send cash reward fail] user:{}, loan:{}, cash:{}.", loginName, loanId, cashAmount);
            return;
        }

        String hkey = MessageFormat.format(HKEY, String.valueOf(loanId), loginName);
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cashAmount),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.LOAN_OUT_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("loan out send cash reward success, loginName:{}, loanId:{}, sendCash:{}", loginName, loanId, cashAmount);
                redisWrapperClient.hset(LOAN_OUT_SUCCESS_SEND_REWARD_KEY, hkey, "success", lifeSecond);
                return;
            }
        } catch (Exception e) {
            logger.error("loan out send cash reward fail, loginName:{}, loanId:{}, sendCash{}", loginName, loanId, cashAmount);
        }
        redisWrapperClient.hset(LOAN_OUT_SUCCESS_SEND_REWARD_KEY, hkey, "fail", lifeSecond);
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("【标的放款发放返现奖励】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(cashAmount)));
        logger.info("loan out send cash reward end, loginName:{}, loanId:{}, sendCash:{}", loginName, loanId, cashAmount);
    }
}
