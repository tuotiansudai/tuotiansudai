package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
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
        if (loanModel == null || loanModel.getStatus() != LoanStatus.CANCEL) {
            logger.error("failed to cancel loan, loan {}", message.getLoanId());
            return;
        }

        loanMapper.updateStatus(loanModel.getId(), LoanStatus.CANCEL);

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        for (InvestModel successInvest : successInvests) {
            successInvest.setStatus(InvestStatus.CANCEL_INVEST_PAYBACK);
            investMapper.update(successInvest);
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, Lists.newArrayList(
                    new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                            successInvest.getLoginName(),
                            successInvest.getId(),
                            message.getBankOrderNo(),
                            message.getBankOrderDate(),
                            successInvest.getAmount(),
                            UserBillBusinessType.CANCEL_INVEST_PAYBACK)
            ));
        }
    }
}
