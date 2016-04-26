package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.ReferrerRewardService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Service
public class ReferrerRewardServiceImpl implements ReferrerRewardService {

    static Logger logger = Logger.getLogger(ReferrerRewardServiceImpl.class);

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

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

    @Value("#{'${pay.user.reward}'.split('\\|')}")
    private List<Double> referrerUserRoleReward;

    @Value("#{'${pay.staff.reward}'.split('\\|')}")
    private List<Double> referrerStaffRoleReward;

    @Override
    @Transactional
    public void rewardReferrer(LoanModel loanModel, List<InvestModel> successInvestList) {
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
                            this.transferReferrerReward(model);
                        }
                    } catch (Exception e) {
                        logger.error(MessageFormat.format("Referrer reward is failed (investId={0} referrerLoginName={1})",
                                String.valueOf(invest.getId()),
                                referrerLoginName));
                    }
                }
            }
        }
    }

    private void transferReferrerReward(InvestReferrerRewardModel model) {
        String referrerLoginName = model.getReferrerLoginName();
        long orderId = model.getId();
        long amount = model.getAmount();

        AccountModel accountModel = accountMapper.findByLoginName(referrerLoginName);
        if (accountModel == null) {
            model.setStatus(ReferrerRewardStatus.NO_ACCOUNT);
            investReferrerRewardMapper.create(model);
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
            investReferrerRewardMapper.create(model);
            if (model.getStatus() == ReferrerRewardStatus.SUCCESS) {
                amountTransfer.transferInBalance(referrerLoginName, orderId, amount, UserBillBusinessType.REFERRER_REWARD, null, null);
                InvestModel investModel = investMapper.findById(model.getInvestId());
                String detail = MessageFormat.format(SystemBillDetailTemplate.REFERRER_REWARD_DETAIL_TEMPLATE.getTemplate(), referrerLoginName, investModel.getLoginName(), String.valueOf(model.getInvestId()));
                systemBillService.transferOut(orderId, amount, SystemBillBusinessType.REFERRER_REWARD, detail);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("referrer reward transfer in balance failed (investId = {0})", String.valueOf(model.getInvestId())));
        }
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

    private int calculateLoanDuration(LoanModel loanModel) {
        int periods = loanModel.getPeriods();
        return periods * InterestCalculator.DAYS_OF_MONTH;
    }

    private Role getReferrerPriorityRole(String referrerLoginName) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(referrerLoginName);

        if (CollectionUtils.isEmpty(userRoleModels)) {
            return null;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), new Predicate<UserRoleModel>() {
            @Override
            public boolean apply(UserRoleModel input) {
                return input.getRole() == Role.STAFF;
            }
        }).isPresent()) {
            return Role.STAFF;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), new Predicate<UserRoleModel>() {
            @Override
            public boolean apply(UserRoleModel input) {
                return input.getRole() == Role.INVESTOR;
            }
        }).isPresent()) {
            return Role.INVESTOR;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), new Predicate<UserRoleModel>() {
            @Override
            public boolean apply(UserRoleModel input) {
                return input.getRole() == Role.USER;
            }
        }).isPresent()) {
            return Role.USER;
        }

        return null;
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
