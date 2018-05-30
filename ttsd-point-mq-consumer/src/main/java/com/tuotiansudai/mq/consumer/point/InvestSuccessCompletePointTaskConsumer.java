package com.tuotiansudai.mq.consumer.point;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.PointTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InvestSuccessCompletePointTaskConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessCompletePointTaskConsumer.class);

    private final PointTaskService pointTaskService;

    private final PointService pointService;

    @Autowired
    public InvestSuccessCompletePointTaskConsumer(PointTaskService pointTaskService, PointService pointService) {
        this.pointTaskService = pointTaskService;
        this.pointService = pointService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Invest_CompletePointTask;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.info("[MQ] receive message is empty");
            return;
        }

        try {
            BankLoanInvestMessage bankLoanInvestMessage = new Gson().fromJson(message, BankLoanInvestMessage.class);

            pointTaskService.completeNewbieTask(PointTask.FIRST_INVEST, bankLoanInvestMessage.getLoginName());
            pointTaskService.completeAdvancedTask(PointTask.EACH_SUM_INVEST, bankLoanInvestMessage.getLoginName());
            pointTaskService.completeAdvancedTask(PointTask.FIRST_SINGLE_INVEST, bankLoanInvestMessage.getLoginName());
            pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_INVEST, bankLoanInvestMessage.getLoginName());
            pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_180, bankLoanInvestMessage.getLoginName());
            pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_360, bankLoanInvestMessage.getLoginName());
            pointService.obtainPointInvest(bankLoanInvestMessage.getInvestId());
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] parse message failed, message: {}", message);
            return;
        }

        logger.info("[MQ] consume message success.");
    }
}
