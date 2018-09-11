package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanFullSuccessSmsInvestorMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanFullSuccessSmsInvestorMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    private static Gson gson = new GsonBuilder().create();

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanFull_SmsInvestor;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankLoanFullMessage bankLoanFullMessage = gson.fromJson(message, BankLoanFullMessage.class);
            LoanModel loanModel = loanMapper.findById(bankLoanFullMessage.getLoanId());
            List<String> mobiles = investMapper.findInvestorMobileByLoanId(loanModel.getId());
            if (CollectionUtils.isNotEmpty(mobiles)){
                String rate = String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100) + "%";
                mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_LOAN_OUT_COMPLETE_NOTIFY_TEMPLATE, mobiles, Lists.newArrayList(loanModel.getName(), rate)));
            }

        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[MQ] parse message failure, message: {0}", message), e);
        }
    }
}
