package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class InvestSuccessExperienceRepayMessageConsumer implements MessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(InvestSuccessExperienceRepayMessageConsumer.class);

    private final static long INVEST_LIMIT = 100000L;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ExperienceRepay;
    }

    @Override
    public void consume(String message) {
        logger.info("[新手体验项目收益发放MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[新手体验项目收益发放MQ] InvestSuccess_ExperienceRepay receive message is empty");
            return;
        }

        try {
            InvestSuccessMessage investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            long investId = investSuccessMessage.getInvestInfo().getInvestId();
            if (!isExperienceInterestConditionAvailable(investId)) {
                logger.info("[新手体验项目收益发放MQ] 条件不符合，{}", investId);
                return;
            }

            logger.info("[新手体验项目收益发放MQ] 条件符合，{}", investId);

            BaseDto<PayDataDto> baseDto = payWrapperClient.experienceRepay(investId);

            if (!baseDto.isSuccess()) {
                logger.error("[新手体验项目收益发放MQ] consume fail. message: " + message);
                throw new RuntimeException("[新手体验项目收益发放MQ] consume fail. message: " + message);
            }
            if (!baseDto.getData().getStatus()) {
                logger.error("[新手体验项目收益发放MQ] 新手体验项目收益发放失败. 出借ID:{}", String.valueOf(investId));
                mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("[新手体验项目收益发放MQ]新手体验项目收益发放失败,出借ID:{0}", String.valueOf(investId)));
                return;
            }
        } catch (Exception e) {
            logger.error("[新手体验项目收益发放MQ] experience repay is fail, message:{}", message);
        }

        logger.info("[新手体验项目收益发放MQ] receive message: {}: {} done.", this.queue(), message);
    }

    private boolean isExperienceInterestConditionAvailable(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        if (investModel == null) {
            logger.info("[新手体验项目收益发放MQ] 出借ID{}不存在", investId);
            return false;
        }
        long investAmount = investMapper.sumSuccessInvestAmountByLoginName(null, investModel.getLoginName(), false);
        if (investAmount < INVEST_LIMIT) {
            return false;
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, 1);

        return investRepayModel != null
                && investRepayModel.getStatus() == RepayStatus.REPAYING
                && new DateTime(investRepayModel.getRepayDate()).isBefore(new DateTime().withTimeAtStartOfDay().plusDays(1));
    }
}
