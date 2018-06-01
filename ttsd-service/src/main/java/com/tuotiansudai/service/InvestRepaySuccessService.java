package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanCallbackMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class InvestRepaySuccessService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(InvestRepaySuccessService.class);

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final InvestRepayMapper investRepayMapper;

    private final LoanRepayMapper loanRepayMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public InvestRepaySuccessService(LoanMapper loanMapper, MQWrapperClient mqWrapperClient, LoanRepayMapper loanRepayMapper, InvestMapper investMapper, InvestRepayMapper investRepayMapper) {
        this.loanMapper = loanMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.loanRepayMapper = loanRepayMapper;
        this.investMapper = investMapper;
        this.investRepayMapper = investRepayMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processNormalInvestRepaySuccess(BankLoanCallbackMessage bankLoanCallbackMessage) {
        InvestModel investModel = investMapper.findById(bankLoanCallbackMessage.getInvestId());
        InvestRepayModel investRepayModel = investRepayMapper.findById(bankLoanCallbackMessage.getInvestRepayId());
        if (!Lists.newArrayList(RepayStatus.REPAYING, RepayStatus.OVERDUE).contains(investRepayModel.getStatus())) {
            logger.error("[Normal Invest Callback Success] invest: {}, invest repay: {},  status is {}", bankLoanCallbackMessage.getInvestId(), bankLoanCallbackMessage.getInvestRepayId(), investRepayModel.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), investRepayModel.getPeriod());

        investRepayModel.setActualInterest(bankLoanCallbackMessage.getInterest());
        investRepayModel.setActualFee(bankLoanCallbackMessage.getInterestFee());
        investRepayModel.setRepayAmount(bankLoanCallbackMessage.getCorpus() + bankLoanCallbackMessage.getInterest() + bankLoanCallbackMessage.getDefaultInterest() - bankLoanCallbackMessage.getInterestFee());
        investRepayModel.setActualRepayDate(loanRepayModel.getActualRepayDate());
        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(investRepayModel);

        // interest user bill
        AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                investModel.getLoginName(),
                bankLoanCallbackMessage.getInvestRepayId(),
                bankLoanCallbackMessage.getCorpus() + bankLoanCallbackMessage.getInterest() + bankLoanCallbackMessage.getDefaultInterest(),
                investRepayModel.getActualRepayDate().before(investRepayModel.getRepayDate()) ? UserBillBusinessType.NORMAL_REPAY : UserBillBusinessType.OVERDUE_REPAY);
        // fee user bill
        AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE,
                investModel.getLoginName(),
                bankLoanCallbackMessage.getInvestRepayId(),
                investRepayModel.getActualFee(),
                UserBillBusinessType.INVEST_FEE);
        inAtm.setNext(outAtm);
        try {
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, inAtm);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("[Normal Invest Callback Success] amount transfer exception, invest: {0}, invest repay: {1}", bankLoanCallbackMessage.getInvestId(), bankLoanCallbackMessage.getInvestRepayId()), ex);
        }

        try {
            //Title:您投资的{0}已回款{1}元，请前往账户查收！
            //Content:尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。
            String title = MessageFormat.format(MessageEventType.REPAY_SUCCESS.getTitleTemplate(), loanModel.getName(), AmountConverter.convertCentToString(investRepayModel.getRepayAmount()));
            String content = MessageFormat.format(MessageEventType.REPAY_SUCCESS.getContentTemplate(), loanModel.getName());
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REPAY_SUCCESS,
                    Lists.newArrayList(investModel.getLoginName()), title, content, bankLoanCallbackMessage.getInvestRepayId()));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()), PushSource.ALL, PushType.REPAY_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));
            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(), WeChatMessageType.NORMAL_REPAY_SUCCESS, bankLoanCallbackMessage.getInvestRepayId()));
        } catch (Exception ignore) {
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void processAdvancedInvestRepaySuccess(BankLoanCallbackMessage bankLoanCallbackMessage) {
        InvestModel investModel = investMapper.findById(bankLoanCallbackMessage.getInvestId());
        InvestRepayModel investRepayModel = investRepayMapper.findById(bankLoanCallbackMessage.getInvestRepayId());

        if (!Lists.newArrayList(RepayStatus.REPAYING, RepayStatus.OVERDUE).contains(investRepayModel.getStatus())) {
            logger.error("[Advanced Invest Callback Success] invest: {}, invest repay: {},  status is {}", bankLoanCallbackMessage.getInvestId(), bankLoanCallbackMessage.getInvestRepayId(), investRepayModel.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), investRepayModel.getPeriod());

        investRepayModel.setActualInterest(bankLoanCallbackMessage.getInterest());
        investRepayModel.setActualFee(bankLoanCallbackMessage.getInterestFee());
        investRepayModel.setRepayAmount(bankLoanCallbackMessage.getCorpus() + bankLoanCallbackMessage.getInterest() + bankLoanCallbackMessage.getDefaultInterest() - bankLoanCallbackMessage.getInterestFee());
        investRepayModel.setActualRepayDate(loanRepayModel.getActualRepayDate());
        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(investRepayModel);

        // update other REPAYING invest repay
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
        investRepayModels.stream().filter(item -> item.getStatus() == RepayStatus.REPAYING).forEach(item -> {
            item.setStatus(RepayStatus.COMPLETE);
            item.setActualRepayDate(loanRepayModel.getActualRepayDate());
            investRepayMapper.update(item);
        });

        // interest user bill
        AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                investModel.getLoginName(),
                bankLoanCallbackMessage.getInvestRepayId(),
                bankLoanCallbackMessage.getCorpus() + bankLoanCallbackMessage.getInterest(),
                UserBillBusinessType.ADVANCE_REPAY);
        // fee user bill
        AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE,
                investModel.getLoginName(),
                bankLoanCallbackMessage.getInvestRepayId(),
                bankLoanCallbackMessage.getInterestFee(),
                UserBillBusinessType.INVEST_FEE);
        inAtm.setNext(outAtm);
        try {
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, inAtm);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("[Advance Invest Callback Success] amount transfer exception, invest: {0}, invest repay: {1}", bankLoanCallbackMessage.getInvestId(), bankLoanCallbackMessage.getInvestRepayId()), ex);
        }

        try {
            //Title:您投资的{0}提前还款，{1}元已返还至您的账户！
            //Content:尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】
            String title = MessageFormat.format(MessageEventType.ADVANCED_REPAY.getTitleTemplate(), loanModel.getName(), AmountConverter.convertCentToString(investRepayModel.getRepayAmount()));
            String content = MessageFormat.format(MessageEventType.ADVANCED_REPAY.getContentTemplate(), loanModel.getName());
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.ADVANCED_REPAY,
                    Lists.newArrayList(investModel.getLoginName()), title, content, bankLoanCallbackMessage.getInvestRepayId()));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()), PushSource.ALL, PushType.ADVANCED_REPAY, title, AppUrl.MESSAGE_CENTER_LIST));
            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(), WeChatMessageType.ADVANCE_REPAY_SUCCESS, bankLoanCallbackMessage.getInvestRepayId()));
        } catch (Exception ignore) {
        }
    }
}
