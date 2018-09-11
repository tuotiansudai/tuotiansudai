package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanCancelMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class LoanCancelService {

    private final static Logger logger = LoggerFactory.getLogger(LoanCancelService.class);

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public LoanCancelService(LoanMapper loanMapper, InvestMapper investMapper, MQWrapperClient mqWrapperClient) {
        this.loanMapper = loanMapper;
        this.investMapper = investMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Transactional
    public void cancel(BankLoanCancelMessage message) {
        LoanModel loanModel = loanMapper.findById(message.getLoanId());
        if (loanModel == null || loanModel.getStatus() == LoanStatus.CANCEL) {
            logger.error("[Loan Cancel] failed to cancel loan, loan {}", message.getLoanId());
            return;
        }

        loanMapper.updateStatus(loanModel.getId(), LoanStatus.CANCEL);
        logger.error("[Loan Cancel] update loan to cancel, loan {}", message.getLoanId());

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        for (InvestModel successInvest : successInvests) {
            successInvest.setStatus(InvestStatus.CANCEL_INVEST_PAYBACK);
            investMapper.update(successInvest);
            logger.info("[Loan Cancel] update invest to cancel, investId: {}, amount: {}", successInvest.getId(), successInvest.getAmount());
        }

        for (InvestModel successInvest : successInvests) {
            try {
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, Lists.newArrayList(
                        new AmountTransferMessage(successInvest.getId(),
                                successInvest.getLoginName(),
                                Role.INVESTOR,
                                successInvest.getAmount(),
                                message.getBankOrderNo(),
                                message.getBankOrderDate(),
                                BillOperationType.IN,
                                BankUserBillBusinessType.CANCEL_INVEST_PAYBACK)));
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Loan Cancel] failed to send message for pay back invest amount, investId: {0}, amount: {1}", String.valueOf(successInvest.getId()), String.valueOf(successInvest.getAmount())), e);
            }
        }
    }
}
