package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class LoanStartRaisingMessageConsumer implements MessageConsumer {
    static Logger logger = LoggerFactory.getLogger(LoanStartRaisingMessageConsumer.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanStartRaising;
    }

    @Override
    public void consume(String message) {
        logger.info("trigger FundraisingStartJob, prepare do job");
        long loanId = Long.parseLong(message);
        logger.info(MessageFormat.format("trigger FundraisingStartJob, loanId = {0}", String.valueOf(loanId)));

        LoanModel loanModel = loanMapper.findById(loanId);
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
        if (loanModel != null && LoanStatus.PREHEAT == loanModel.getStatus()) {
            loanMapper.updateStatus(loanId, LoanStatus.RAISING);
            if (!Strings.isNullOrEmpty(loanDetailsModel.getPushMessage())) {
                mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(null, PushSource.ALL, PushType.PREHEAT, loanDetailsModel.getPushMessage(), AppUrl.INVEST_NORMAL, Maps.newLinkedHashMap()));
            }
        }
    }
}
