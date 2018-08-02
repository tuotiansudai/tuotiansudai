package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WeChatMessageType;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanerDetailsMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class InvestSuccessCheckLoanFullMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessCheckLoanFullMessageConsumer.class);

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("#{'${loan.raising.complete.notify.mobiles}'.split('\\|')}")
    private List<String> loanRaisingCompleteNotifyMobileList;

    @Autowired
    private JobManager jobManager;

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
                //设置一个延迟任务;
                DelayMessageDeliveryJobCreator.createAutoLoanOutDelayJob(jobManager, loanModel.getId());
                //短信通知
                sendLoanRaisingCompleteNotify(loanModel);
            }
        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }

    private void sendLoanRaisingCompleteNotify(LoanModel loanModel) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM月dd日");

        String loanRaisingStartDate = sdfDate.format(loanModel.getFundraisingStartTime());

        String loanName = loanModel.getName();

        long loanAmount = loanModel.getLoanAmount();
        String loanAmountStr; // 单位：万
        if (loanAmount % 1000000 == 0)
            loanAmountStr = String.valueOf(loanAmount / 1000000);
        else
            loanAmountStr = String.valueOf((double) (loanAmount / 10000) / 100);

        String loanDuration = String.valueOf(loanModel.getDuration());

        String loanerName = loanModel == null ? "" : loanModel.getLoanerUserName();

        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        String agentUserName = agentModel == null ? "" : agentModel.getUserName();

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH点mm分");
        String loanRaisingCompleteTime = sdfTime.format(loanModel.getRaisingCompleteTime());

        logger.info("will send loan raising complete notify, loanId:" + loanModel.getId());

        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE, loanRaisingCompleteNotifyMobileList,
                Lists.newArrayList(loanRaisingStartDate, loanDuration, loanAmountStr, loanRaisingCompleteTime, loanerName, agentUserName)));

        mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(null, WeChatMessageType.LOAN_COMPLETE, loanModel.getId()));

    }
}
