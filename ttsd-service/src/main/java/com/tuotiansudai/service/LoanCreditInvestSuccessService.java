package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanCreditInvestMessage;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanCreditInvestSuccessService {

    private static Logger logger = Logger.getLogger(LoanCreditInvestSuccessService.class);

    private final InvestMapper investMapper;

    private final TransferApplicationMapper transferApplicationMapper;

    private final InvestRepayMapper investRepayMapper;

    private final UserMapper userMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public LoanCreditInvestSuccessService(InvestMapper investMapper, TransferApplicationMapper transferApplicationMapper, UserMapper userMapper, MQWrapperClient mqWrapperClient, InvestRepayMapper investRepayMapper) {
        this.investMapper = investMapper;
        this.transferApplicationMapper = transferApplicationMapper;
        this.userMapper = userMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.investRepayMapper = investRepayMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processLoanCreditInvestSuccess(BankLoanCreditInvestMessage bankLoanCreditInvestMessage) {

        long transferApplicationId = bankLoanCreditInvestMessage.getTransferApplicationId();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);

        if (transferApplicationModel == null || transferApplicationModel.getStatus() != TransferStatus.TRANSFERRING) {
            logger.warn(MessageFormat.format("[MQ] loanCreditInvest transferApplicationModel not found or status is incorrect, message: {0}", new Gson().toJson(bankLoanCreditInvestMessage)));
            return;
        }

        InvestModel transferInvestModel = investMapper.findById(transferApplicationModel.getTransferInvestId());

        if (transferInvestModel == null || transferInvestModel.getTransferStatus() != TransferStatus.TRANSFERRING) {
            logger.error(MessageFormat.format("[MQ] loanCreditInvest transferInvestModel not found or status is incorrect, message: {0}", new Gson().toJson(bankLoanCreditInvestMessage)));
            return;
        }

        InvestModel investModel = investMapper.findById(bankLoanCreditInvestMessage.getInvestId());

        if (investModel == null || investModel.getStatus() != InvestStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[MQ] loanCreditInvest investModel not found or status is incorrect, message: {0}", new Gson().toJson(bankLoanCreditInvestMessage)));
            return;
        }

        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setInvestId(bankLoanCreditInvestMessage.getInvestId());
        transferApplicationModel.setTransferTime(investModel.getCreatedTime());
        transferApplicationMapper.update(transferApplicationModel);

        investMapper.updateTransferStatus(transferInvestModel.getId(), TransferStatus.SUCCESS);

        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setBankOrderNo(bankLoanCreditInvestMessage.getBankOrderNo());
        investModel.setBankOrderDate(bankLoanCreditInvestMessage.getBankOrderDate());
        investModel.setTradingTime(new Date());
        investMapper.update(investModel);

        //更新 (承接人&转让人) 还款计划
        this.updateInvestRepay(transferApplicationModel);

        //承接人购买成功 转出
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                Lists.newArrayList(new AmountTransferMessage(transferApplicationId,
                        investModel.getLoginName(),
                        Role.INVESTOR,
                        transferApplicationModel.getTransferAmount(),
                        bankLoanCreditInvestMessage.getBankOrderNo(),
                        bankLoanCreditInvestMessage.getBankOrderDate(),
                        BillOperationType.OUT,
                        BankUserBillBusinessType.INVEST_TRANSFER_IN)));

        //转让人转让成功 转入
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                Lists.newArrayList(
                        new AmountTransferMessage(transferApplicationId,
                                transferInvestModel.getLoginName(),
                                Role.INVESTOR,
                                transferApplicationModel.getTransferAmount(),
                                bankLoanCreditInvestMessage.getBankOrderNo(),
                                bankLoanCreditInvestMessage.getBankOrderDate(),
                                BillOperationType.IN,
                                BankUserBillBusinessType.INVEST_TRANSFER_OUT),
                        new AmountTransferMessage(transferApplicationId,
                                transferInvestModel.getLoginName(),
                                Role.INVESTOR,
                                transferApplicationModel.getTransferFee(),
                                bankLoanCreditInvestMessage.getBankOrderNo(),
                                bankLoanCreditInvestMessage.getBankOrderDate(),
                                BillOperationType.OUT,
                                BankUserBillBusinessType.TRANSFER_FEE)));

        //系统账户收取手续费
        mqWrapperClient.sendMessage(MessageQueue.BankSystemBill,
                new BankSystemBillMessage(
                        BillOperationType.IN,
                        transferApplicationId,
                        bankLoanCreditInvestMessage.getBankOrderNo(),
                        bankLoanCreditInvestMessage.getBankOrderDate(),
                        transferApplicationModel.getTransferFee(),
                        SystemBillBusinessType.TRANSFER_FEE,
                        MessageFormat.format(SystemBillDetailTemplate.TRANSFER_FEE_DETAIL_TEMPLATE.getTemplate(), transferInvestModel.getLoginName(), String.valueOf(transferApplicationId), String.valueOf(transferApplicationModel.getTransferFee()))));

        this.sendMessage(transferApplicationModel);
    }

    private void updateInvestRepay(TransferApplicationModel transferApplicationModel) {
        long transferInvestId = transferApplicationModel.getTransferInvestId();
        long investId = transferApplicationModel.getInvestId();
        InvestModel investModel = investMapper.findById(investId);
        final int transferBeginWithPeriod = transferApplicationModel.getPeriod();

        List<InvestRepayModel> transferrerTransferredInvestRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(transferInvestId).stream().filter(investRepayModel -> investRepayModel.getPeriod() >= transferBeginWithPeriod).collect(Collectors.toList());

        List<InvestRepayModel> transfereeInvestRepayModels = transferrerTransferredInvestRepayModels.stream().map(transferrerTransferredInvestRepayModel -> {
            long expectedFee = new BigDecimal(transferrerTransferredInvestRepayModel.getExpectedInterest()).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investModel.getInvestFeeRate())).longValue();
            InvestRepayModel investRepayModel = new InvestRepayModel(IdGenerator.generate(),
                    investId,
                    transferrerTransferredInvestRepayModel.getPeriod(),
                    transferrerTransferredInvestRepayModel.getCorpus(),
                    transferrerTransferredInvestRepayModel.getExpectedInterest(),
                    expectedFee,
                    transferrerTransferredInvestRepayModel.getRepayDate(),
                    transferrerTransferredInvestRepayModel.getStatus());

            transferrerTransferredInvestRepayModel.setExpectedInterest(0);
            transferrerTransferredInvestRepayModel.setExpectedFee(0);
            transferrerTransferredInvestRepayModel.setCorpus(0);
            transferrerTransferredInvestRepayModel.setTransferred(true);
            transferrerTransferredInvestRepayModel.setStatus(RepayStatus.COMPLETE);

            investRepayMapper.update(transferrerTransferredInvestRepayModel);

            return investRepayModel;

        }).collect(Collectors.toList());

        investRepayMapper.create(transfereeInvestRepayModels);
    }

    private void sendMessage(TransferApplicationModel transferApplicationModel) {

        try {
            //Title:您发起的转让项目转让成功，{0}元已发放至您的账户！
            //Content:尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。

            String title = MessageFormat.format(MessageEventType.TRANSFER_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount()));
            String content = MessageFormat.format(MessageEventType.TRANSFER_SUCCESS.getContentTemplate(), transferApplicationModel.getName());
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.TRANSFER_SUCCESS,
                    Lists.newArrayList(transferApplicationModel.getLoginName()),
                    title,
                    content,
                    transferApplicationModel.getId()));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(transferApplicationModel.getLoginName()),
                    PushSource.ALL,
                    PushType.TRANSFER_SUCCESS,
                    title,
                    AppUrl.MESSAGE_CENTER_LIST));

            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(transferApplicationModel.getLoginName(), WeChatMessageType.TRANSFER_SUCCESS, transferApplicationModel.getId()));

            // 安心签
            mqWrapperClient.sendMessage(MessageQueue.TransferAnxinContract, new AnxinContractMessage(transferApplicationModel.getId(), AnxinContractType.TRANSFER_CONTRACT.name()));

            String mobile = userMapper.findByLoginName(transferApplicationModel.getLoginName()).getMobile();
            mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_TRANSFER_LOAN_SUCCESS_TEMPLATE, Lists.newArrayList(mobile), Lists.newArrayList(transferApplicationModel.getName())));

        } catch (Exception e) {
            logger.error("loanCreditInvest success message notify send fail", e);
        }

    }
}
