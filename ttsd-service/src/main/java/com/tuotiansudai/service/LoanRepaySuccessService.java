package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.fudian.umpmessage.UmpLoanRepayMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.UmpAmountTransferMessage;
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
    public void processNormalLoanRepaySuccess(BankLoanRepayMessage bankLoanRepayMessage) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(bankLoanRepayMessage.getLoanRepayId());

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error("[Normal Loan Repay Success] loan: {}, loan repay: {},  status is {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());
        // update current loan repay status
        currentLoanRepay.setStatus(RepayStatus.COMPLETE);
        currentLoanRepay.setBankOrderNo(bankLoanRepayMessage.getBankOrderNo());
        currentLoanRepay.setBankOrderDate(bankLoanRepayMessage.getBankOrderDate());
        loanRepayMapper.update(currentLoanRepay);
        logger.info("[Normal Loan Repay Success] update loan repay status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        LoanRepayModel lastLoanRepay = loanRepayMapper.findLastLoanRepay(loanModel.getId());
        if (lastLoanRepay.getId() == currentLoanRepay.getId()) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
            logger.info("[Normal Loan Repay Success] last loan repay, update loan status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());
        }

        // update loaner user bill
        BankUserBillBusinessType businessType = loanModel.getStatus() == LoanStatus.OVERDUE ? BankUserBillBusinessType.OVERDUE_REPAY : BankUserBillBusinessType.NORMAL_REPAY;
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                Lists.newArrayList(new AmountTransferMessage(currentLoanRepay.getId(),
                        loanModel.getAgentLoginName(),
                        Role.LOANER,
                        currentLoanRepay.getRepayAmount(),
                        bankLoanRepayMessage.getBankOrderNo(),
                        bankLoanRepayMessage.getBankOrderDate(),
                        BillOperationType.OUT,
                        businessType)));
        logger.info("[Normal Loan Repay Success] update user bill, loan: {}, loan repay: {}, amount: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getRepayAmount());
    }

    @Transactional(rollbackFor = Exception.class)
    public void processAdvancedLoanRepaySuccess(BankLoanRepayMessage bankLoanRepayMessage) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(bankLoanRepayMessage.getLoanRepayId());

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
                loanRepayModel.setBankOrderNo(bankLoanRepayMessage.getBankOrderNo());
                loanRepayModel.setBankOrderDate(bankLoanRepayMessage.getBankOrderDate());
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
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                Lists.newArrayList(new AmountTransferMessage(currentLoanRepay.getId(),
                        loanModel.getAgentLoginName(),
                        Role.LOANER,
                        currentLoanRepay.getRepayAmount(),
                        bankLoanRepayMessage.getBankOrderNo(),
                        bankLoanRepayMessage.getBankOrderDate(),
                        BillOperationType.OUT,
                        BankUserBillBusinessType.ADVANCE_REPAY)));
        logger.info("[Advanced Loan Repay Success] update user bill, loan: {}, loan repay: {}, amount: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getRepayAmount());
    }

    @Transactional(rollbackFor = Exception.class)
    public void processUmpNormalLoanRepaySuccess(UmpLoanRepayMessage umpLoanRepayMessage) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(umpLoanRepayMessage.getLoanRepayId());

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error("[UMP Normal Loan Repay Success] loan: {}, loan repay: {},  status is {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());
        // update current loan repay status
        currentLoanRepay.setStatus(RepayStatus.COMPLETE);
        loanRepayMapper.update(currentLoanRepay);
        logger.info("[UMP Normal Loan Repay Success] update loan repay status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        LoanRepayModel lastLoanRepay = loanRepayMapper.findLastLoanRepay(loanModel.getId());
        if (lastLoanRepay.getId() == currentLoanRepay.getId()) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
            logger.info("[UMP Normal Loan Repay Success] last loan repay, update loan status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());
        }

        // update loaner user bill
        UserBillBusinessType businessType = loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY;
        mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer,
                Lists.newArrayList(new UmpAmountTransferMessage(
                        UmpTransferType.TRANSFER_OUT_BALANCE,
                        umpLoanRepayMessage.getLoginName(),
                        umpLoanRepayMessage.getLoanRepayId(),
                        umpLoanRepayMessage.getAmount(),
                        businessType)));

        logger.info("[UMP Normal Loan Repay Success] update user bill, loan: {}, loan repay: {}, amount: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getRepayAmount());
    }

    @Transactional(rollbackFor = Exception.class)
    public void processUmpAdvancedLoanRepaySuccess(UmpLoanRepayMessage umpLoanRepayMessage) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(umpLoanRepayMessage.getLoanRepayId());

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error("[UMP Advanced Loan Repay Success] loan: {}, loan repay: {},  status is {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());

        // update other loan repay status
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() != RepayStatus.COMPLETE) {
                loanRepayModel.setActualRepayDate(currentLoanRepay.getActualRepayDate());
                loanRepayModel.setStatus(RepayStatus.COMPLETE);
                loanRepayMapper.update(loanRepayModel);
            }
        }
        logger.info("[UMP Advanced Loan Repay Success] update all loan repay status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
        logger.info("[UMP Advanced Loan Repay Success] update loan status COMPLETE, loan: {}, loan repay: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId());

        List<TransferApplicationModel> transferringApplications = transferApplicationMapper.findAllTransferringApplicationsByLoanId(loanModel.getId());
        transferringApplications.forEach(transferringApplication -> {
            transferringApplication.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferringApplication);
        });

        // update agent user bill
        mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer,
                Lists.newArrayList(new UmpAmountTransferMessage(
                        UmpTransferType.TRANSFER_OUT_BALANCE,
                        loanModel.getAgentLoginName(),
                        currentLoanRepay.getId(),
                        currentLoanRepay.getRepayAmount(),
                        UserBillBusinessType.ADVANCE_REPAY)));
        logger.info("[UMP Advanced Loan Repay Success] update user bill, loan: {}, loan repay: {}, amount: {}", currentLoanRepay.getLoanId(), currentLoanRepay.getId(), currentLoanRepay.getRepayAmount());
    }
}
