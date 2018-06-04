package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.LoanPeriodCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LoanFullService {

    private final static Logger logger = LoggerFactory.getLogger(LoanFullService.class);

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public LoanFullService(LoanMapper loanMapper, InvestMapper investMapper, MQWrapperClient mqWrapperClient) {
        this.loanMapper = loanMapper;
        this.investMapper = investMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Transactional
    public void processLoanFull(BankLoanFullMessage bankLoanFullMessage) {
        logger.info("[Loan Full] process loan full, loanId: {}", bankLoanFullMessage.getLoanId());

        LoanModel loanModel = loanMapper.findById(bankLoanFullMessage.getLoanId());

        if (loanModel == null || loanModel.getStatus() != LoanStatus.RECHECK) {
            logger.error("[Loan Full] loan is not exist or status is not RECHECK");
            return;
        }

        investMapper.cleanWaitingInvest(loanModel.getId());

        this.updateLoanStatus(loanModel, bankLoanFullMessage);

        this.sendMessage(loanModel);

        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                        loanModel.getAgentLoginName(),
                        loanModel.getId(),
                        loanModel.getLoanAmount(),
                        UserBillBusinessType.LOAN_SUCCESS));
    }

    private void updateLoanStatus(LoanModel loanModel, BankLoanFullMessage bankLoanFullMessage) {
        loanModel.setRecheckTime(new Date());
        loanModel.setPeriods(LoanPeriodCalculator.calculateLoanPeriods(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType()));
        loanModel.setStatus(LoanStatus.REPAYING);
        loanModel.setLoanFullBankOrderNo(bankLoanFullMessage.getBankOrderNo());
        loanModel.setLoanFullBankOrderDate(bankLoanFullMessage.getBankOrderDate());
        loanMapper.update(loanModel);
        logger.info("[Loan Full] update loan status to REPAYING, loanId: {}", loanModel.getId());
    }

    private void sendMessage(LoanModel loanModel) {
        //Title:您投资的{0}已经满额放款，预期年化收益{1}%
        //Content:尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。
        try {
            List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
            String title = MessageFormat.format(MessageEventType.LOAN_OUT_SUCCESS.getTitleTemplate(), loanModel.getName(), (loanModel.getBaseRate() + loanModel.getActivityRate()) * 100);
            String content = MessageFormat.format(MessageEventType.LOAN_OUT_SUCCESS.getContentTemplate(), loanModel.getName(), (loanModel.getBaseRate() + loanModel.getActivityRate()) * 100);

            List<String> loginNames = Lists.newArrayList();
            Map<Long, String> investIdLoginNames = Maps.newHashMap();
            for (InvestModel investModel : investModels) {
                loginNames.add(investModel.getLoginName());
                investIdLoginNames.put(investModel.getId(), investModel.getLoginName());
            }

            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.LOAN_OUT_SUCCESS, title, content, investIdLoginNames));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(loginNames, PushSource.ALL, PushType.LOAN_OUT_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));

            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(null, WeChatMessageType.LOAN_OUT_SUCCESS, loanModel.getId()));
        } catch (Exception ex) {
            logger.warn(ex.getLocalizedMessage(), ex);
        }
    }
}
