package com.tuotiansudai.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanRepaySuccessService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LoanRepaySuccessService.class);

    private final LoanMapper loanMapper;

    private final LoanRepayMapper loanRepayMapper;

    private final TransferApplicationMapper transferApplicationMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public LoanRepaySuccessService(LoanMapper loanMapper, TransferApplicationMapper transferApplicationMapper, LoanRepayMapper loanRepayMapper, MQWrapperClient mqWrapperClient) {
        this.loanMapper = loanMapper;
        this.loanRepayMapper = loanRepayMapper;
        this.transferApplicationMapper = transferApplicationMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processNormalLoanRepaySuccess(BankLoanRepayMessage message) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(message.getLoanRepayId());

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error("[Normal Loan Repay Success] loan: {}, loan repay: {},  status is {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());
        // update current loan repay status
        currentLoanRepay.setStatus(RepayStatus.COMPLETE);
        currentLoanRepay.setBankOrderNo(message.getBankOrderNo());
        currentLoanRepay.setBankOrderDate(message.getBankOrderDate());
        loanRepayMapper.update(currentLoanRepay);
        logger.info("[Normal Loan Repay Success] update loan repay status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        LoanRepayModel lastLoanRepay = loanRepayMapper.findLastLoanRepay(loanModel.getId());
        if (lastLoanRepay.getId() == currentLoanRepay.getId()) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
            logger.info("[Normal Loan Repay Success] last loan repay, update loan status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());
        }

        // update agent user bill
        UserBillBusinessType businessType = loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY;
        AmountTransferMessage amountTransferMessage = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loanModel.getAgentLoginName(), message.getLoanRepayId(), currentLoanRepay.getRepayAmount(), businessType);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, amountTransferMessage);
        logger.info("[Normal Loan Repay Success] update user bill, loan: {}, loan repay: {}, amount: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getRepayAmount());
    }

    @Transactional(rollbackFor = Exception.class)
    public void processAdvancedLoanRepaySuccess(BankLoanRepayMessage message) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(message.getLoanRepayId());

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error("[Advanced Loan Repay Success] loan: {}, loan repay: {},  status is {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());

        // update other loan repay status
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() != RepayStatus.COMPLETE) {
                loanRepayModel.setActualRepayDate(currentLoanRepay.getActualRepayDate());
                loanRepayModel.setStatus(RepayStatus.COMPLETE);
                loanRepayModel.setBankOrderNo(message.getBankOrderNo());
                loanRepayModel.setBankOrderDate(message.getBankOrderDate());
                loanRepayMapper.update(loanRepayModel);
            }
        }
        logger.info("[Advanced Loan Repay Success] update all loan repay status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
        logger.info("[Advanced Loan Repay Success] update loan status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        List<TransferApplicationModel> transferringApplications = transferApplicationMapper.findAllTransferringApplicationsByLoanId(loanModel.getId());
        transferringApplications.forEach(transferringApplication -> {
            transferringApplication.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferringApplication);
        });

        // update agent user bill
        AmountTransferMessage amountTransferMessage = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE,
                loanModel.getAgentLoginName(),
                message.getLoanRepayId(),
                currentLoanRepay.getRepayAmount(),
                UserBillBusinessType.ADVANCE_REPAY);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, amountTransferMessage);
        logger.info("[Advanced Loan Repay Success] update user bill, loan: {}, loan repay: {}, amount: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getRepayAmount());
    }
}
