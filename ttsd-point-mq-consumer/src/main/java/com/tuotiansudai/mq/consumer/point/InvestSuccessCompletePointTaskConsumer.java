package com.tuotiansudai.mq.consumer.point;

import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class InvestSuccessCompletePointTaskConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessCompletePointTaskConsumer.class);

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private PointService pointService;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_CompletePointTask;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: '{}'.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            logger.info("[MQ] ready to consume message: complete invest task.");
            InvestSuccessMessage investSuccessMessage;
            try {
                investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            } catch (IOException e) {
                logger.error("[MQ] parse message failed: {}: '{}'.", this.queue(), message);
                return;
            }

            InvestModel investModel = investMapper.findById(investSuccessMessage.getInvestInfo().getInvestId());
            if (investModel != null && investModel.getStatus() == InvestStatus.SUCCESS) {
                String loginName = investModel.getLoginName();
                pointTaskService.completeNewbieTask(PointTask.FIRST_INVEST, loginName);
                pointTaskService.completeAdvancedTask(PointTask.EACH_SUM_INVEST, loginName);
                pointTaskService.completeAdvancedTask(PointTask.FIRST_SINGLE_INVEST, loginName);
                pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_INVEST, loginName);
                pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_180, loginName);
                pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_360, loginName);
                pointService.obtainPointInvest(investModel);
            }
            logger.info("[MQ] consume message success.");
        }
    }
}
