package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanCallbackMessage;
import com.tuotiansudai.fudian.umpmessage.UmpCouponRepayMessage;
import com.tuotiansudai.fudian.umpmessage.UmpExtraRepayMessage;
import com.tuotiansudai.fudian.umpmessage.UmpRepayFeeMessage;
import com.tuotiansudai.fudian.umpmessage.UmpRepayPaybackMessage;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestRepaySuccessService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(InvestRepaySuccessService.class);

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final InvestRepayMapper investRepayMapper;

    private final LoanRepayMapper loanRepayMapper;

    private final CouponMapper couponMapper;

    private final CouponRepayMapper couponRepayMapper;

    private final InvestExtraRateMapper investExtraRateMapper;

    private final MQWrapperClient mqWrapperClient;

    private final UserMapper userMapper;

    @Autowired
    public InvestRepaySuccessService(LoanMapper loanMapper, MQWrapperClient mqWrapperClient, LoanRepayMapper loanRepayMapper, InvestMapper investMapper, InvestRepayMapper investRepayMapper, CouponMapper couponMapper, CouponRepayMapper couponRepayMapper, InvestExtraRateMapper investExtraRateMapper, UserMapper userMapper) {
        this.loanMapper = loanMapper;
        this.mqWrapperClient = mqWrapperClient;
        this.loanRepayMapper = loanRepayMapper;
        this.investMapper = investMapper;
        this.investRepayMapper = investRepayMapper;
        this.couponMapper = couponMapper;
        this.couponRepayMapper = couponRepayMapper;
        this.investExtraRateMapper = investExtraRateMapper;
        this.userMapper = userMapper;
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
        investRepayModel.setBankOrderNo(bankLoanCallbackMessage.getBankOrderNo());
        investRepayModel.setBankOrderDate(bankLoanCallbackMessage.getBankOrderDate());
        investRepayMapper.update(investRepayModel);

        try {
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, Lists.newArrayList(
                    new AmountTransferMessage(investRepayModel.getId(),
                            investModel.getLoginName(),
                            Role.INVESTOR,
                            bankLoanCallbackMessage.getCorpus() + bankLoanCallbackMessage.getInterest() + bankLoanCallbackMessage.getDefaultInterest(),
                            bankLoanCallbackMessage.getBankOrderNo(),
                            bankLoanCallbackMessage.getBankOrderDate(),
                            BillOperationType.IN,
                            investRepayModel.getActualRepayDate().before(investRepayModel.getRepayDate()) ? BankUserBillBusinessType.NORMAL_REPAY : BankUserBillBusinessType.OVERDUE_REPAY),
                    new AmountTransferMessage(investRepayModel.getId(),
                            investModel.getLoginName(),
                            Role.INVESTOR,
                            investRepayModel.getActualFee(),
                            bankLoanCallbackMessage.getBankOrderNo(),
                            bankLoanCallbackMessage.getBankOrderDate(),
                            BillOperationType.OUT,
                            BankUserBillBusinessType.INVEST_FEE)));

            mqWrapperClient.sendMessage(MessageQueue.BankSystemBill,
                    new BankSystemBillMessage(BillOperationType.IN,
                            investRepayModel.getId(),
                            bankLoanCallbackMessage.getBankOrderNo(),
                            bankLoanCallbackMessage.getBankOrderDate(),
                            investRepayModel.getActualFee(),
                            SystemBillBusinessType.INVEST_FEE,
                            MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), String.valueOf(loanModel.getId()), String.valueOf(loanRepayModel.getId()))));
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
        investRepayModel.setBankOrderNo(bankLoanCallbackMessage.getBankOrderNo());
        investRepayModel.setBankOrderDate(bankLoanCallbackMessage.getBankOrderDate());
        investRepayMapper.update(investRepayModel);

        // update other REPAYING invest repay
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
        investRepayModels.stream().filter(item -> item.getStatus() == RepayStatus.REPAYING).forEach(item -> {
            item.setStatus(RepayStatus.COMPLETE);
            item.setActualRepayDate(loanRepayModel.getActualRepayDate());
            item.setBankOrderNo(bankLoanCallbackMessage.getBankOrderNo());
            item.setBankOrderDate(bankLoanCallbackMessage.getBankOrderDate());
            investRepayMapper.update(item);
        });

        try {
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, Lists.newArrayList(
                    new AmountTransferMessage(investRepayModel.getId(),
                            investModel.getLoginName(),
                            Role.INVESTOR,
                            bankLoanCallbackMessage.getCorpus() + bankLoanCallbackMessage.getInterest(),
                            bankLoanCallbackMessage.getBankOrderNo(),
                            bankLoanCallbackMessage.getBankOrderDate(),
                            BillOperationType.IN,
                            BankUserBillBusinessType.ADVANCE_REPAY),
                    new AmountTransferMessage(investRepayModel.getId(),
                            investModel.getLoginName(),
                            Role.INVESTOR,
                            bankLoanCallbackMessage.getInterestFee(),
                            bankLoanCallbackMessage.getBankOrderNo(),
                            bankLoanCallbackMessage.getBankOrderDate(),
                            BillOperationType.OUT,
                            BankUserBillBusinessType.INVEST_FEE)));

            mqWrapperClient.sendMessage(MessageQueue.BankSystemBill,
                    new BankSystemBillMessage(
                            BillOperationType.IN,
                            investRepayModel.getId(),
                            bankLoanCallbackMessage.getBankOrderNo(),
                            bankLoanCallbackMessage.getBankOrderDate(),
                            investRepayModel.getActualFee(),
                            SystemBillBusinessType.INVEST_FEE,
                            MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), String.valueOf(loanModel.getId()), String.valueOf(loanRepayModel.getId()))));
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
            mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_ADVANCED_REPAY_TEMPLATE, Lists.newArrayList(userMapper.findByLoginName(investModel.getLoginName()).getMobile()), Lists.newArrayList(loanModel.getName())));
        } catch (Exception ignore) {
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void processUmpNormalInvestRepaySuccess(UmpRepayPaybackMessage umpRepayPaybackMessage) {
        InvestModel investModel = investMapper.findById(umpRepayPaybackMessage.getInvestId());
        InvestRepayModel investRepayModel = investRepayMapper.findById(umpRepayPaybackMessage.getInvestRepayId());
        if (!Lists.newArrayList(RepayStatus.REPAYING, RepayStatus.OVERDUE).contains(investRepayModel.getStatus())) {
            logger.error("[UMP Normal Invest Callback Success] invest: {}, invest repay: {},  status is {}", umpRepayPaybackMessage.getInvestId(), umpRepayPaybackMessage.getInvestRepayId(), investRepayModel.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), investRepayModel.getPeriod());

        investRepayModel.setActualInterest(umpRepayPaybackMessage.getInterest());
        investRepayModel.setActualFee(umpRepayPaybackMessage.getInterest());
        investRepayModel.setRepayAmount(umpRepayPaybackMessage.getCorpus() + umpRepayPaybackMessage.getInterest() + umpRepayPaybackMessage.getDefaultInterest() - umpRepayPaybackMessage.getFee());
        investRepayModel.setActualRepayDate(loanRepayModel.getActualRepayDate());
        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(investRepayModel);

        try {
            mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer, Lists.newArrayList(
                    new UmpAmountTransferMessage(
                            UmpTransferType.TRANSFER_IN_BALANCE,
                            investModel.getLoginName(),
                            investRepayModel.getId(),
                            umpRepayPaybackMessage.getCorpus() + umpRepayPaybackMessage.getInterest() + umpRepayPaybackMessage.getDefaultInterest(),
                            investRepayModel.getActualRepayDate().before(investRepayModel.getRepayDate()) ? UserBillBusinessType.NORMAL_REPAY : UserBillBusinessType.OVERDUE_REPAY),
                    new UmpAmountTransferMessage(
                            UmpTransferType.TRANSFER_OUT_BALANCE,
                            investModel.getLoginName(),
                            investRepayModel.getId(),
                            investRepayModel.getActualFee(),
                            UserBillBusinessType.INVEST_FEE)));
        } catch (Exception ex) {
            logger.error(MessageFormat.format("[UMP Normal Invest Callback Success] amount transfer exception, invest: {0}, invest repay: {1}", umpRepayPaybackMessage.getInvestId(), umpRepayPaybackMessage.getInvestRepayId()), ex);
        }

        try {
            //Title:您投资的{0}已回款{1}元，请前往账户查收！
            //Content:尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。
            String title = MessageFormat.format(MessageEventType.REPAY_SUCCESS.getTitleTemplate(), loanModel.getName(), AmountConverter.convertCentToString(investRepayModel.getRepayAmount()));
            String content = MessageFormat.format(MessageEventType.REPAY_SUCCESS.getContentTemplate(), loanModel.getName());
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REPAY_SUCCESS,
                    Lists.newArrayList(investModel.getLoginName()), title, content, umpRepayPaybackMessage.getInvestRepayId()));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()), PushSource.ALL, PushType.REPAY_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));
            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(), WeChatMessageType.NORMAL_REPAY_SUCCESS, umpRepayPaybackMessage.getInvestRepayId()));
        } catch (Exception ignore) {
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void processUmpAdvancedInvestRepaySuccess(UmpRepayPaybackMessage umpRepayPaybackMessage) {
        InvestModel investModel = investMapper.findById(umpRepayPaybackMessage.getInvestId());
        InvestRepayModel investRepayModel = investRepayMapper.findById(umpRepayPaybackMessage.getInvestRepayId());

        if (!Lists.newArrayList(RepayStatus.REPAYING, RepayStatus.OVERDUE).contains(investRepayModel.getStatus())) {
            logger.error("[UMP Advanced Invest Callback Success] invest: {}, invest repay: {},  status is {}", umpRepayPaybackMessage.getInvestId(), umpRepayPaybackMessage.getInvestRepayId(), investRepayModel.getStatus());
            return;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), investRepayModel.getPeriod());

        investRepayModel.setActualInterest(umpRepayPaybackMessage.getInterest());
        investRepayModel.setActualFee(umpRepayPaybackMessage.getFee());
        investRepayModel.setRepayAmount(umpRepayPaybackMessage.getCorpus() + umpRepayPaybackMessage.getInterest() + umpRepayPaybackMessage.getDefaultInterest() - umpRepayPaybackMessage.getFee());
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

        try {
            mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer, Lists.newArrayList(
                    new UmpAmountTransferMessage(
                            UmpTransferType.TRANSFER_IN_BALANCE,
                            investModel.getLoginName(),
                            investRepayModel.getId(),
                            umpRepayPaybackMessage.getCorpus() + umpRepayPaybackMessage.getInterest(),
                            UserBillBusinessType.ADVANCE_REPAY),
                    new UmpAmountTransferMessage(
                            UmpTransferType.TRANSFER_OUT_BALANCE,
                            investModel.getLoginName(),
                            investRepayModel.getId(),
                            umpRepayPaybackMessage.getFee(),
                            UserBillBusinessType.INVEST_FEE)));
        } catch (Exception ex) {
            logger.error(MessageFormat.format("[Advance Invest Callback Success] amount transfer exception, invest: {0}, invest repay: {1}", umpRepayPaybackMessage.getInvestId(), umpRepayPaybackMessage.getInvestRepayId()), ex);
        }

        try {
            //Title:您投资的{0}提前还款，{1}元已返还至您的账户！
            //Content:尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】
            String title = MessageFormat.format(MessageEventType.ADVANCED_REPAY.getTitleTemplate(), loanModel.getName(), AmountConverter.convertCentToString(investRepayModel.getRepayAmount()));
            String content = MessageFormat.format(MessageEventType.ADVANCED_REPAY.getContentTemplate(), loanModel.getName());
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.ADVANCED_REPAY,
                    Lists.newArrayList(investModel.getLoginName()), title, content, umpRepayPaybackMessage.getInvestRepayId()));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()), PushSource.ALL, PushType.ADVANCED_REPAY, title, AppUrl.MESSAGE_CENTER_LIST));
            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(), WeChatMessageType.ADVANCE_REPAY_SUCCESS, umpRepayPaybackMessage.getInvestRepayId()));
            mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_ADVANCED_REPAY_TEMPLATE, Lists.newArrayList(userMapper.findByLoginName(investModel.getLoginName()).getMobile()), Lists.newArrayList(loanModel.getName())));
        } catch (Exception ignore) {
        }
    }

    public void processCouponRepaySuccess(UmpCouponRepayMessage umpCouponRepayMessage) {
        CouponRepayModel couponRepayModel = couponRepayMapper.findById(umpCouponRepayMessage.getCouponRepayId());
        InvestModel investModel = investMapper.findById(couponRepayModel.getInvestId());
        CouponModel couponModel = couponMapper.findById(couponRepayModel.getCouponId());

        try {
            if (couponRepayModel.getStatus() != RepayStatus.REPAYING) {
                return;
            }
            couponRepayModel.setActualInterest(umpCouponRepayMessage.getInterest());
            couponRepayModel.setActualFee(umpCouponRepayMessage.getFee());
            couponRepayModel.setRepayAmount(umpCouponRepayMessage.getInterest() - umpCouponRepayMessage.getFee());
            couponRepayModel.setActualRepayDate(new Date());
            couponRepayModel.setStatus(RepayStatus.COMPLETE);
            couponRepayMapper.update(couponRepayModel);
            if (!umpCouponRepayMessage.isNormalRepay()) {
                List<CouponRepayModel> advancedCouponRepayModels = couponRepayMapper.findByUserCouponByInvestId(investModel.getId())
                        .stream()
                        .filter(input -> input.getPeriod() > couponRepayModel.getPeriod())
                        .collect(Collectors.toList());

                for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                    if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                        advancedCouponRepayModel.setActualRepayDate(new Date());
                        advancedCouponRepayModel.setStatus(RepayStatus.COMPLETE);
                        couponRepayMapper.update(advancedCouponRepayModel);
                        logger.info(MessageFormat.format("[Advance Coupon Repay] update other REPAYING coupon repay({0}) status to COMPLETE",
                                String.valueOf(advancedCouponRepayModel.getId())));
                    }
                }
            }

            mqWrapperClient.sendMessage(MessageQueue.UmpAmountTransfer, Lists.newArrayList(
                    new UmpAmountTransferMessage(UmpTransferType.TRANSFER_IN_BALANCE,
                            couponRepayModel.getLoginName(),
                            couponRepayModel.getUserCouponId(),
                            couponRepayModel.getActualInterest(),
                            couponModel.getCouponType().getUserBillBusinessType()),
                    new UmpAmountTransferMessage(UmpTransferType.TRANSFER_OUT_BALANCE,
                            couponRepayModel.getLoginName(),
                            couponRepayModel.getUserCouponId(),
                            couponRepayModel.getActualFee(),
                            UserBillBusinessType.INVEST_FEE)));

            String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                    couponModel.getCouponType().getName(),
                    String.valueOf(couponRepayModel.getUserCouponId()),
                    String.valueOf(couponRepayModel.getId()),
                    String.valueOf(couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()));

            mqWrapperClient.sendMessage(MessageQueue.SystemBill, new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT,
                    couponRepayModel.getUserCouponId(),
                    (couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()), SystemBillBusinessType.COUPON, detail));

            logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill and system bill is success",
                    String.valueOf(couponRepayModel.getId()),
                    String.valueOf(couponRepayModel.getUserCouponId())));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill is failed",
                    String.valueOf(couponRepayModel.getId()),
                    String.valueOf(couponRepayModel.getUserCouponId())), e);
        }
    }

    @Transactional
    public void processExtraRepaySuccess(UmpExtraRepayMessage umpExtraRepayMessage) {
        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findById(umpExtraRepayMessage.getInvestExtraRateId());
        if (investExtraRateModel.getStatus() == RepayStatus.COMPLETE) {
            return;
        }

        long amount = umpExtraRepayMessage.getInterest() - umpExtraRepayMessage.getFee();
        investExtraRateModel.setActualInterest(umpExtraRepayMessage.getInterest());
        investExtraRateModel.setActualFee(umpExtraRepayMessage.getFee());
        investExtraRateModel.setRepayAmount(amount);
        investExtraRateModel.setActualRepayDate(new Date());
        investExtraRateModel.setStatus(RepayStatus.COMPLETE);
        investExtraRateMapper.update(investExtraRateModel);

        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, new UmpAmountTransferMessage(UmpTransferType.TRANSFER_IN_BALANCE,
                umpExtraRepayMessage.getLoginName(),
                umpExtraRepayMessage.getInvestExtraRateId(),
                amount,
                UserBillBusinessType.EXTRA_RATE));

        mqWrapperClient.sendMessage(MessageQueue.SystemBill, new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT,
                investExtraRateModel.getId(),
                amount,
                SystemBillBusinessType.EXTRA_RATE,
                MessageFormat.format(SystemBillDetailTemplate.EXTRA_RATE_DETAIL_TEMPLATE.getTemplate(),
                        investExtraRateModel.getLoginName(), String.valueOf(investExtraRateModel.getInvestId()))));
    }

    @Transactional
    public void processRepayFeeSuccess(UmpRepayFeeMessage umpRepayFeeMessage) {
        mqWrapperClient.sendMessage(MessageQueue.SystemBill, new SystemBillMessage(SystemBillMessageType.TRANSFER_IN,
                umpRepayFeeMessage.getLoanRepayId(),
                umpRepayFeeMessage.getFee(),
                SystemBillBusinessType.INVEST_FEE,
                MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(),
                        String.valueOf(umpRepayFeeMessage.getLoanId()), String.valueOf(umpRepayFeeMessage.getLoanRepayId()))));
    }
}
