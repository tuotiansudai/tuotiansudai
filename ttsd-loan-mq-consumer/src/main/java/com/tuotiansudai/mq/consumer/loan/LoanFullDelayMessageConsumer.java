package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class LoanFullDelayMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanFullDelayMessageConsumer.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BankAccountMapper bankAccountMapper;
    @Autowired
    private LoanMapper loanMapper;

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanFullDelay;
    }


    @Override
    public void consume(String message) {
        logger.info("trigger auto loan out after raising complete job, prepare do job");
        try {
            long loanId = Long.parseLong(message);
            logger.info("trigger auto loan out after raising complete job, loanId : " + String.valueOf(loanId));
            LoanModel loanModel = loanMapper.findById(loanId);
            if (LoanStatus.RECHECK.equals(loanModel.getStatus())) {
                UserModel agentUser = userMapper.findByLoginName(loanModel.getAgentLoginName());
                BankAccountModel agentAccount = bankAccountMapper.findByLoginNameAndRole(loanModel.getAgentLoginName(), Role.LOANER);
                BankBaseMessage loanFullMessage = bankWrapperClient.loanFull(agentUser.getLoginName(),
                        agentUser.getMobile(),
                        agentAccount.getBankUserName(),
                        agentAccount.getBankAccountNo(),
                        loanModel.getId(),
                        loanModel.getLoanTxNo(),
                        loanModel.getBankOrderNo(),
                        loanModel.getBankOrderDate(),
                        new DateTime(loanModel.getDeadline()).toString("yyyyMMdd"),
                        null,
                        new DateTime().plusMinutes(1).getMillis());

                if (!loanFullMessage.isStatus()) {
                    throw new RuntimeException(MessageFormat.format("[MQ] request loan full is failure, message: {0},returnMsg:{}", message, loanFullMessage.getMessage()));
                }
            }

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }
}
