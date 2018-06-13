package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class InvestSuccessCheckLoanFullMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessCheckLoanFullMessageConsumer.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final Gson gson  = new GsonBuilder().create();

    @Autowired
    public InvestSuccessCheckLoanFullMessageConsumer(UserMapper userMapper, BankAccountMapper bankAccountMapper, LoanMapper loanMapper, InvestMapper investMapper) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.loanMapper = loanMapper;
        this.investMapper = investMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.Invest_CheckLoanFull;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankLoanInvestMessage bankLoanInvestMessage = gson.fromJson(message, BankLoanInvestMessage.class);

            LoanModel loanModel = loanMapper.findById(bankLoanInvestMessage.getLoanId());
            InvestModel investModel = investMapper.findById(bankLoanInvestMessage.getInvestId());
            List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

            long sumInvestAmount = successInvests.stream().filter(successInvest -> successInvest.getId() != investModel.getId()).mapToLong(InvestModel::getAmount).sum();

            if (sumInvestAmount + investModel.getAmount() == loanModel.getLoanAmount()) {
                loanMapper.updateRaisingCompleteTime(loanModel.getId());
                UserModel agentUser = userMapper.findByLoginName(loanModel.getAgentLoginName());
                BankAccountModel agentAccount = bankAccountMapper.findByLoginName(loanModel.getAgentLoginName());
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
                    throw new RuntimeException(MessageFormat.format("[MQ] request loan full is failure, message: {0}", message));
                }
            }
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }
}
