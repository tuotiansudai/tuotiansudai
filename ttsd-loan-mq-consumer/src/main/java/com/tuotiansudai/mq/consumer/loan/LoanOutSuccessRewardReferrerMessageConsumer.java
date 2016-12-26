package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.LoanOutMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessRewardReferrerMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessRewardReferrerMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private AgentLevelRateMapper agentLevelRateMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private SystemBillMapper systemBillMapper;

    @Value("#{'${pay.user.reward}'.split('\\|')}")
    private List<Double> referrerUserRoleReward;

    @Value("#{'${pay.staff.reward}'.split('\\|')}")
    private List<Double> referrerStaffRoleReward;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_RewardReferrer;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutMessage loanOutMessage;
            try {
                loanOutMessage = JsonConverter.readValue(message, LoanOutMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long loanId = loanOutMessage.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[标的放款]：处理推荐人奖励，标的ID:" + loanId);

            if (!rewardReferrer(loanId)) {
                fatalSmsList.add("发放推荐人奖励失败");
                logger.error(MessageFormat.format("[标的放款]:发放推荐人奖励失败 (loanId = {0})", String.valueOf(loanId)));
            }

            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess_RewardReferrer fail sms sending. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info("[MQ] consume LoanOutSuccess_RewardReferrer success.");
        }
    }

    private boolean rewardReferrer(long loanId) {
        boolean result = true;
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);

        int loanDuration = this.calculateLoanDuration(loanModel);

        for (InvestModel invest : successInvestList) {
            List<ReferrerRelationModel> referrerRelationList = referrerRelationMapper.findByLoginName(invest.getLoginName());
            for (ReferrerRelationModel referrerRelationModel : referrerRelationList) {
                String referrerLoginName = referrerRelationModel.getReferrerLoginName();
                if (investReferrerRewardMapper.findByInvestIdAndReferrer(invest.getId(), referrerLoginName) == null) {
                    try {
                        Role role = this.getReferrerPriorityRole(referrerLoginName);
                        if (role != null) {
                            long reward = this.calculateReferrerReward(invest.getAmount(), loanDuration, referrerRelationModel.getLevel(), role, referrerLoginName);
                            InvestReferrerRewardModel model = new InvestReferrerRewardModel(idGenerator.generate(), invest.getId(), reward, referrerLoginName, role);
                            investReferrerRewardMapper.create(model);
                            this.transferReferrerReward(model);
                        }
                    } catch (Exception e) {
                        logger.error(MessageFormat.format("Referrer reward is failed (investId={0} referrerLoginName={1})",
                                String.valueOf(invest.getId()),
                                referrerLoginName));
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    private int calculateLoanDuration(LoanModel loanModel) {
        if (loanModel.getType().getLoanPeriodUnit() == LoanPeriodUnit.DAY) {
            return loanModel.getPeriods();
        }

        int periods = loanModel.getPeriods();
        return periods * InterestCalculator.DAYS_OF_MONTH;
    }

    private void transferReferrerReward(InvestReferrerRewardModel model) {
        String referrerLoginName = model.getReferrerLoginName();
        long orderId = model.getId();
        long amount = model.getAmount();

        AccountModel accountModel = accountMapper.findByLoginName(referrerLoginName);
        if (accountModel == null) {
            model.setStatus(ReferrerRewardStatus.NO_ACCOUNT);
            investReferrerRewardMapper.update(model);
            logger.error(MessageFormat.format("referrer has no account, investId={0} referrerLoginName={1} referrerRole={2} amount={3}",
                    String.valueOf(model.getInvestId()),
                    model.getReferrerLoginName(),
                    model.getReferrerRole().name(),
                    String.valueOf(model.getAmount())));
            return;
        }

        if (amount == 0) {
            model.setStatus(ReferrerRewardStatus.SUCCESS);
        }

        if (amount > 0) {
            try {
                TransferRequestModel requestModel = TransferRequestModel.newRequest(String.valueOf(orderId), accountModel.getPayUserId(), String.valueOf(amount));
                TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                model.setStatus(responseModel.isSuccess() ? ReferrerRewardStatus.SUCCESS : ReferrerRewardStatus.FAILURE);
            } catch (Exception e) {
                logger.error(MessageFormat.format("referrer reward is failed, investId={0} referrerLoginName={1} referrerRole={2} amount={3}",
                        String.valueOf(model.getInvestId()),
                        model.getReferrerLoginName(),
                        model.getReferrerRole().name(),
                        String.valueOf(model.getAmount())), e);
                model.setStatus(ReferrerRewardStatus.FAILURE);
            }
        }

        try {
            investReferrerRewardMapper.update(model);
            if (model.getStatus() == ReferrerRewardStatus.SUCCESS) {
                logger.info(MessageFormat.format("[标的放款]:发送推荐人奖励,推荐人:{0},投资ID:{1},推荐人奖励:{2}", referrerLoginName, orderId, amount));
                amountTransfer.transferInBalance(referrerLoginName, orderId, amount, UserBillBusinessType.REFERRER_REWARD, null, null);
                InvestModel investModel = investMapper.findById(model.getInvestId());
                String detail = MessageFormat.format(SystemBillDetailTemplate.REFERRER_REWARD_DETAIL_TEMPLATE.getTemplate(), referrerLoginName, investModel.getLoginName(), String.valueOf(model.getInvestId()));
                logger.info(MessageFormat.format("[标的放款]:记录系统奖励,投资ID:{0},推荐人奖励:{1},奖励类型:{2}", orderId, amount, SystemBillBusinessType.REFERRER_REWARD));
                transferOut(orderId, amount, SystemBillBusinessType.REFERRER_REWARD, detail);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("referrer reward transfer in balance failed (investId = {0})", String.valueOf(model.getInvestId())));
        }
    }

    public void transferOut(long orderId, long amount, SystemBillBusinessType businessType, String detail) {
        SystemBillModel systemBillModel = new SystemBillModel(orderId, amount, SystemBillOperationType.OUT, businessType, detail);
        systemBillMapper.create(systemBillModel);
    }

    private Role getReferrerPriorityRole(String referrerLoginName) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(referrerLoginName);

        if (org.apache.commons.collections4.CollectionUtils.isEmpty(userRoleModels)) {
            return null;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), input -> input.getRole() == Role.STAFF).isPresent()) {
            return Role.STAFF;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), input -> input.getRole() == Role.INVESTOR).isPresent()) {
            return Role.INVESTOR;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), input -> input.getRole() == Role.USER).isPresent()) {
            return Role.USER;
        }

        return null;
    }

    private long calculateReferrerReward(long amount, int loanDuration, int level, Role role, String referrerLoginName) {
        BigDecimal amountBigDecimal = new BigDecimal(amount);

        double rewardRate = this.getRewardRate(level, Role.STAFF == role, referrerLoginName);

        return amountBigDecimal
                .multiply(new BigDecimal(rewardRate))
                .multiply(new BigDecimal(loanDuration))
                .divide(new BigDecimal(InterestCalculator.DAYS_OF_YEAR), 0, BigDecimal.ROUND_DOWN)
                .longValue();
    }

    private double getRewardRate(int level, boolean isStaff, String referrerLoginName) {
        if (isStaff) {
            AgentLevelRateModel agentLevelRateModel = agentLevelRateMapper.findAgentLevelRateByLoginNameAndLevel(referrerLoginName, level);
            if (agentLevelRateModel != null) {
                double agentRewardRate = agentLevelRateModel.getRate();
                if (agentRewardRate > 0) {
                    return agentRewardRate;
                }
            }
            return level > this.referrerStaffRoleReward.size() ? 0 : this.referrerStaffRoleReward.get(level - 1);
        }

        return level > this.referrerUserRoleReward.size() ? 0 : this.referrerUserRoleReward.get(level - 1);
    }
}
