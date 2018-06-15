package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.fudian.message.BankMerchantTransferMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.LoanPeriodCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReferrerRewardService {

    private final static Logger logger = LoggerFactory.getLogger(ReferrerRewardService.class);

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final UserRoleMapper userRoleMapper;

    private final InvestMapper investMapper;

    private final LoanMapper loanMapper;

    private final ReferrerRelationMapper referrerRelationMapper;

    private final InvestReferrerRewardMapper investReferrerRewardMapper;

    private final MQWrapperClient mqWrapperClient;

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    public ReferrerRewardService(UserMapper userMapper, BankAccountMapper bankAccountMapper, UserRoleMapper userRoleMapper, InvestMapper investMapper, LoanMapper loanMapper, ReferrerRelationMapper referrerRelationMapper, InvestReferrerRewardMapper investReferrerRewardMapper, MQWrapperClient mqWrapperClient) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.userRoleMapper = userRoleMapper;
        this.investMapper = investMapper;
        this.loanMapper = loanMapper;
        this.referrerRelationMapper = referrerRelationMapper;
        this.investReferrerRewardMapper = investReferrerRewardMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    public void rewardReferrer(BankLoanFullMessage bankLoanFullMessage) {
        LoanModel loanModel = loanMapper.findById(bankLoanFullMessage.getLoanId());
        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(bankLoanFullMessage.getLoanId());
        Date loanDealLine = loanModel.getDeadline();

        for (InvestModel invest : successInvestList) {
            List<ReferrerRelationModel> referrerRelationList = referrerRelationMapper.findByLoginName(invest.getLoginName());
            for (ReferrerRelationModel referrerRelationModel : referrerRelationList) {
                String referrerLoginName = referrerRelationModel.getReferrerLoginName();
                try {
                    if (investReferrerRewardMapper.findByInvestIdAndReferrer(invest.getId(), referrerLoginName) != null) {
                        logger.warn("[Loan Full] referrer reward record is exist, skip. loanId: {}, referrer: {}, investId: {0}", loanModel.getId(), referrerLoginName, invest.getId());
                        continue;
                    }
                    Role role = this.getReferrerPriorityRole(referrerLoginName);
                    if (role == null) {
                        logger.warn("[Loan Full] referrer role check failure, loanId: {}, referrer: {}, investId: {}", loanModel.getId(), referrerLoginName, invest.getId());
                        continue;
                    }
                    long reward = this.calculateReferrerReward(invest.getAmount(), invest.getTradingTime(), loanDealLine, referrerRelationModel.getLevel(), role);
                    InvestReferrerRewardModel model = new InvestReferrerRewardModel(IdGenerator.generate(), invest.getId(), reward, referrerLoginName, role);
                    investReferrerRewardMapper.create(model);
                    this.transferReferrerReward(loanModel, model);
                } catch (Exception e) {
                    logger.warn(MessageFormat.format("[Loan Full] referrer reward exception, loanId:{}, referrer: {0}, investId: {1}", loanModel.getId(), referrerLoginName, String.valueOf(invest.getId())), e);
                }
            }
        }
    }

    private boolean transferReferrerReward(LoanModel loanModel, InvestReferrerRewardModel model) {
        if (Lists.newArrayList(Role.ZC_STAFF, Role.ZC_STAFF_RECOMMEND).contains(model.getReferrerRole())) {
            model.setStatus(ReferrerRewardStatus.FORBIDDEN);
            investReferrerRewardMapper.update(model);
            logger.warn("[Loan Full] forbid reward for ZC_STAFF, ZC_STAFF_RECOMMEND, loanId: {}, investId: {}, referrerLoginName: {}, referrerRole: {}", loanModel.getId(), model.getInvestId(), model.getReferrerLoginName(), model.getReferrerRole().name());
            return false;
        }

        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(model.getReferrerLoginName());
        if (bankAccountModel == null) {
            model.setStatus(ReferrerRewardStatus.NO_ACCOUNT);
            investReferrerRewardMapper.update(model);
            logger.warn("[Loan Full] bank account not found, loanId: {}, investId: {}, referrerLoginName: {}, referrerRole: {}, amount: {}", loanModel.getId(), model.getInvestId(), model.getReferrerLoginName(), model.getReferrerRole().name(), model.getAmount());
            return false;
        }

        if (model.getAmount() == 0) {
            model.setStatus(ReferrerRewardStatus.SUCCESS);
            investReferrerRewardMapper.update(model);
            return false;
        }

        try {
            UserModel userModel = userMapper.findByLoginName(model.getReferrerLoginName());
            BankMerchantTransferMessage bankMerchantTransferMessage = bankWrapperClient.merchantTransfer(model.getReferrerLoginName(), userModel.getMobile(), bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), model.getAmount());
            logger.info("[Loan Full] transfer referrer reward, loanId: {}, referrer: {}, investId: {}, result: {}", loanModel.getId(), model.getReferrerLoginName(), model.getInvestId(), bankMerchantTransferMessage);
            model.setStatus(bankMerchantTransferMessage.isStatus() ? ReferrerRewardStatus.SUCCESS : ReferrerRewardStatus.FAILURE);
            model.setBankOrderNo(bankMerchantTransferMessage.getBankOrderNo());
            model.setBankOrderDate(bankMerchantTransferMessage.getBankOrderDate());
            investReferrerRewardMapper.update(model);
            if (bankMerchantTransferMessage.isStatus()) {
                this.sendMessage(model);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Loan Full] transfer referrer reward exception, loanId: {0}, investId: {1}, referrer: {2}, referrerRole: {3}, amount: {4}",
                    String.valueOf(loanModel.getId()),
                    String.valueOf(model.getInvestId()),
                    model.getReferrerLoginName(),
                    model.getReferrerRole().name(),
                    String.valueOf(model.getAmount())), e);

        }
        return false;

    }

    private long calculateReferrerReward(long amount, Date investTime, Date dealLine, int level, Role role) {
        if (Lists.newArrayList(Role.ZC_STAFF_RECOMMEND).contains(role)) {
            return 0;
        }

        double rewardRate = this.getRewardRate(level, (Role.SD_STAFF == role || Role.ZC_STAFF == role));

        return new BigDecimal(amount)
                .multiply(new BigDecimal(rewardRate))
                .multiply(new BigDecimal(LoanPeriodCalculator.calculateDuration(investTime, dealLine)))
                .divide(new BigDecimal(InterestCalculator.DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN)
                .longValue();
    }

    private Role getReferrerPriorityRole(String referrerLoginName) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(referrerLoginName);
        if (userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == Role.ZC_STAFF)) {
            return Role.ZC_STAFF;
        }
        if (userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == Role.ZC_STAFF_RECOMMEND)) {
            return Role.ZC_STAFF_RECOMMEND;
        }
        if (userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == Role.SD_STAFF)) {
            return Role.SD_STAFF;
        }
        if (userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == Role.INVESTOR)) {
            return Role.INVESTOR;
        }
        if (userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == Role.USER)) {
            return Role.USER;
        }
        return null;
    }

    private double getRewardRate(int level, boolean isSDStaff) {
        if (isSDStaff) {
            return level > 4 ? 0 : 0.005;
        }
        return level > 2 ? 0 : 0.005;
    }

    private void sendMessage(InvestReferrerRewardModel model) {
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                Lists.newArrayList(new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,
                        model.getReferrerLoginName(),
                        model.getId(),
                        model.getBankOrderNo(),
                        model.getBankOrderDate(),
                        model.getAmount(),
                        UserBillBusinessType.REFERRER_REWARD)));

        InvestModel investModel = investMapper.findById(model.getInvestId());
        String detail = MessageFormat.format(SystemBillDetailTemplate.REFERRER_REWARD_DETAIL_TEMPLATE.getTemplate(),
                model.getReferrerLoginName(),
                investModel.getLoginName(),
                String.valueOf(model.getInvestId()));
        mqWrapperClient.sendMessage(MessageQueue.SystemBill,
                new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT,
                        model.getId(),
                        model.getAmount(),
                        SystemBillBusinessType.REFERRER_REWARD, detail));

        logger.info("[Loan Full] referrer reward update bill, referrer: {}, investId: {}, amount: {}", model.getReferrerLoginName(), model.getInvestId(), model.getAmount());

        //Title:{0}元推荐奖励已存入您的账户，请查收！
        //Content:尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。
        String title = MessageFormat.format(MessageEventType.RECOMMEND_AWARD_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(model.getAmount()));
        String content = MessageFormat.format(MessageEventType.RECOMMEND_AWARD_SUCCESS.getContentTemplate(), userMapper.findByLoginName(investModel.getLoginName()).getMobile(), AmountConverter.convertCentToString(model.getAmount()));
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.RECOMMEND_AWARD_SUCCESS,
                Lists.newArrayList(model.getReferrerLoginName()), title, content, model.getId()));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(model.getReferrerLoginName()), PushSource.ALL, PushType.RECOMMEND_AWARD_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));
    }
}
