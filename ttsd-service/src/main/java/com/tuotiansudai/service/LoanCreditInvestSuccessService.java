package com.tuotiansudai.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanCreditInvestMessage;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestExtraRateMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.*;
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

    private final InvestExtraRateMapper investExtraRateMapper;

    private final InvestRepayMapper investRepayMapper;

    private final MQWrapperClient mqWrapperClient;

    private final SmsWrapperClient smsWrapperClient;

    @Autowired
    private LoanCreditInvestSuccessService(InvestMapper investMapper, TransferApplicationMapper transferApplicationMapper, MQWrapperClient mqWrapperClient, InvestExtraRateMapper investExtraRateMapper, InvestRepayMapper investRepayMapper, SmsWrapperClient smsWrapperClient){
        this.investMapper = investMapper;
        this.transferApplicationMapper = transferApplicationMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.investExtraRateMapper = investExtraRateMapper;
        this.investRepayMapper = investRepayMapper;
        this.smsWrapperClient = smsWrapperClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processLoanCreditInvestSuccess(BankLoanCreditInvestMessage bankLoanCreditInvestMessage){

        long transferApplicationId = bankLoanCreditInvestMessage.getTransferApplicationId();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);

        if (transferApplicationModel == null || transferApplicationModel.getStatus() != TransferStatus.TRANSFERRING) {
            logger.warn(MessageFormat.format("[MQ] loanCreditInvest transferApplicationModel not found or status is incorrect, message: {0}", new Gson().toJson(bankLoanCreditInvestMessage)));
            return;
        }

        InvestModel transferInvestModel = investMapper.findById(transferApplicationModel.getTransferInvestId());

        if (transferInvestModel == null || transferInvestModel.getTransferStatus() != TransferStatus.TRANSFERRING ){
            logger.error(MessageFormat.format("[MQ] loanCreditInvest transferInvestModel not found or status is incorrect, message: {0}", new Gson().toJson(bankLoanCreditInvestMessage)));
            return;
        }

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());

        if (investModel == null || investModel.getStatus() != InvestStatus.WAIT_PAY){
            logger.error(MessageFormat.format("[MQ] loanCreditInvest investModel not found or status is incorrect, message: {0}", new Gson().toJson(bankLoanCreditInvestMessage)));
            return;
        }

        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationMapper.update(transferApplicationModel);

        transferInvestModel.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.update(transferInvestModel);

        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setBankOrderNo(bankLoanCreditInvestMessage.getBankOrderNo());
        investModel.setBankOrderDate(bankLoanCreditInvestMessage.getBankOrderDate());
        investModel.setTradingTime(new Date());
        investMapper.update(investModel);

        //更新 (承接人&转让人) 还款计划
        this.updateInvestRepay(transferApplicationModel);

        //承接人购买成功 转出
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, investModel.getLoginName(), transferApplicationId, transferApplicationModel.getTransferAmount(), UserBillBusinessType.INVEST_TRANSFER_IN));

        //转让人转让成功 转入
        AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, transferInvestModel.getLoginName(), transferApplicationId, transferApplicationModel.getTransferAmount(), UserBillBusinessType.INVEST_TRANSFER_OUT);
        AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, transferInvestModel.getLoginName(), transferApplicationId, transferApplicationModel.getTransferFee(), UserBillBusinessType.TRANSFER_FEE);
        inAtm.setNext(outAtm);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, inAtm);

        //系统账户收取手续费
        SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_IN,
                transferApplicationModel.getId(), transferApplicationModel.getTransferFee(), SystemBillBusinessType.TRANSFER_FEE,
                MessageFormat.format(SystemBillDetailTemplate.TRANSFER_FEE_DETAIL_TEMPLATE.getTemplate(), transferInvestModel.getLoginName(), String.valueOf(transferApplicationId), String.valueOf(transferApplicationModel.getTransferFee())));
        mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);

        this.sendMessage(transferApplicationModel);
    }

    private void updateInvestRepay(TransferApplicationModel transferApplicationModel) {
        long transferInvestId = transferApplicationModel.getTransferInvestId();
        long investId = transferApplicationModel.getInvestId();
        InvestModel investModel = investMapper.findById(investId);
        final int transferBeginWithPeriod = transferApplicationModel.getPeriod();

        List<InvestRepayModel> transferrerTransferredInvestRepayModels = Lists.newArrayList(Iterables.filter(investRepayMapper.findByInvestIdAndPeriodAsc(transferInvestId), new Predicate<InvestRepayModel>() {
            @Override
            public boolean apply(InvestRepayModel input) {
                return input.getPeriod() >= transferBeginWithPeriod;
            }
        }));

        List<InvestRepayModel> transfereeInvestRepayModels = transferrerTransferredInvestRepayModels.stream().map(transferrerTransferredInvestRepayModel -> {
            transferrerTransferredInvestRepayModel.setExpectedInterest(0);
            transferrerTransferredInvestRepayModel.setExpectedFee(0);
            transferrerTransferredInvestRepayModel.setCorpus(0);
            transferrerTransferredInvestRepayModel.setTransferred(true);
            transferrerTransferredInvestRepayModel.setStatus(RepayStatus.COMPLETE);

            investRepayMapper.update(transferrerTransferredInvestRepayModel);

            long expectedFee = new BigDecimal(transferrerTransferredInvestRepayModel.getExpectedInterest()).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investModel.getInvestFeeRate())).longValue();

            return new InvestRepayModel(IdGenerator.generate(),
                    investId,
                    transferrerTransferredInvestRepayModel.getPeriod(),
                    transferrerTransferredInvestRepayModel.getCorpus(),
                    transferrerTransferredInvestRepayModel.getExpectedInterest(),
                    expectedFee,
                    transferrerTransferredInvestRepayModel.getRepayDate(),
                    transferrerTransferredInvestRepayModel.getStatus());
        }).collect(Collectors.toList());

        investRepayMapper.create(transfereeInvestRepayModels);
    }

    private void sendMessage(TransferApplicationModel transferApplicationModel) {

        try{
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

        }catch (Exception e){
            logger.error("loanCreditInvest success message notify send fail", e);
        }

    }
}
