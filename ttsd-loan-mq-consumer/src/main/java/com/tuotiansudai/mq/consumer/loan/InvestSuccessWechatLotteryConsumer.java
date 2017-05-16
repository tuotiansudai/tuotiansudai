package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InvestSuccessWechatLotteryConsumer implements MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(InvestSuccessWechatLotteryConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private InvestService investService;

    @Autowired
    private PayWrapperClient payWrapperClient;

    private static final String WECHAT_LOTTERY_COUNT_KEY = "WECHAT_LOTTERY_COUNT:";

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.wechat.lottery.startTime}\")}")
    private Date wechatLotteryStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.wechat.lottery.endTime}\")}")
    private Date wechatLotteryEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_WechatLottery;
    }

    @Override
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        Date now = new Date();

        if (wechatLotteryStartTime.after(now)) {
            logger.info("wechat lottery activity does not started yet. start time is:{}", wechatLotteryStartTime);
            return;
        } else if (wechatLotteryEndTime.before(now)) {
            logger.info("wechat lottery activity is ended. end time is:{}", wechatLotteryEndTime);
            return;
        }

        try {
            InvestSuccessMessage ism = JsonConverter.readValue(message, InvestSuccessMessage.class);

            String loginName = ism.getUserInfo().getLoginName();
            long investAmount = ism.getInvestInfo().getAmount();
            long loanId = ism.getLoanDetailInfo().getLoanId();

            InvestModel invesetModel = investService.findById(ism.getInvestInfo().getInvestId());

            if (investService.isNewUserForWechatLottery(loginName) && invesetModel.getSource() == Source.WE_CHAT && loanId != 1 && investAmount >= 500000) {
                redisWrapperClient.incr(WECHAT_LOTTERY_COUNT_KEY + loginName, investAmount / 500000);
                if (investService.isFirstInvest(loginName, invesetModel.getTradingTime())) {
                    sendCashPrize(loginName, investAmount);
                }
            }

        } catch (Exception e) {
            logger.error("[MQ] wechat lottery consumer fail, message:{}", message);
        }
        logger.info("[MQ] consume message done: {}: {}.", this.queue(), message);

    }


    private void sendCashPrize(String loginName, long investAmount) {
        logger.info("send wechat invest cash prize, loginName:{}, investAmount:{}", loginName, investAmount);

        if (investAmount < 500000) {
            logger.info("invest amount is less than 5000, no prize.");
            return;
        }

        long prizeAmount;
        if (investAmount < 1000000) {
            prizeAmount = 1800;
        } else if (investAmount < 2000000) {
            prizeAmount = 3800;
        } else {
            prizeAmount = 8800;
        }

        long orderId = IdGenerator.generate();
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(orderId), String.valueOf(prizeAmount));
        BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
        logger.info("send wechat invest cash prize, loginName:{}, response:{}", loginName, response.getData().getMessage());
    }
}
