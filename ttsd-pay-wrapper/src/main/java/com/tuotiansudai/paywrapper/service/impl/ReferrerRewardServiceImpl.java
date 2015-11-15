package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.ReferrerRewardService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
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
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Value("${referrer.user.reward}")
    private String referrerUserRoleReward;

    @Value("${referrer.merchandiser.reward}")
    private String referrerMerchandiserRoleReward;

    @Override
    public void rewardReferrer(LoanModel loanModel, List<InvestModel> successInvestList) {
        int loanDuration = this.calculateLoanDuration(loanModel);

        for (InvestModel invest : successInvestList) {
            List<ReferrerRelationModel> referrerRelationList = referrerRelationMapper.findByLoginName(invest.getLoginName());
            if (CollectionUtils.isEmpty(referrerRelationList)) {
                continue;
            }

            for (ReferrerRelationModel referrerRelationModel : referrerRelationList) {
                try {
                    String referrerLoginName = referrerRelationModel.getReferrerLoginName();
                    Role role = this.getReferrerPriorityRole(referrerLoginName);
                    if (role != null) {
                        long reward = this.calculateReferrerReward(invest.getAmount(), loanDuration, referrerRelationModel.getLevel(), role);

                        InvestReferrerRewardModel model = new InvestReferrerRewardModel(idGenerator.generate(), invest.getId(), reward, referrerLoginName, role);

                        this.transferReferrerReward(model);
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @Transactional
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
            model.setStatus(ReferrerRewardStatus.FAILURE);
            investReferrerRewardMapper.create(model);
            logger.error(MessageFormat.format("referrer reward amount is zero, investId={0} referrerLoginName={1} referrerRole={2} amount={3}",
                    String.valueOf(model.getInvestId()),
                    model.getReferrerLoginName(),
                    model.getReferrerRole().name(),
                    String.valueOf(model.getAmount())));
            return;
        }

        try {
            TransferRequestModel requestModel = TransferRequestModel.newReferrerRewardRequest(String.valueOf(orderId), accountModel.getPayUserId(), String.valueOf(amount));
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            if (responseModel.isSuccess()) {
                try {
                    amountTransfer.transferInBalance(referrerLoginName, orderId, amount, UserBillBusinessType.REFERRER_REWARD, null, null);
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("referrer reward transfer in balance failed (investId = {0})", String.valueOf(model.getInvestId())));
                }
                systemBillService.transferOut(amount, String.valueOf(orderId), SystemBillBusinessType.REFERRER_REWARD, SystemBillMessageTemplate.REFERRER_REWARD_MESSAGE_TEMPLATE.getTemplate());
                model.setStatus(ReferrerRewardStatus.SUCCESS);
            } else {
                logger.error(MessageFormat.format("referrer reward is failed, investId={0} referrerLoginName={1} referrerRole={2} amount={3} errorMessage={4}",
                        String.valueOf(model.getInvestId()),
                        model.getReferrerLoginName(),
                        model.getReferrerRole().name(),
                        String.valueOf(model.getAmount()),
                        responseModel.getRetCode()));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("referrer reward is failed, investId={0} referrerLoginName={1} referrerRole={2} amount={3}",
                            String.valueOf(model.getInvestId()),
                            model.getReferrerLoginName(),
                            model.getReferrerRole().name(),
                            String.valueOf(model.getAmount())));
            logger.error(e.getLocalizedMessage(), e);
        }
        investReferrerRewardMapper.create(model);
    }

    private long calculateReferrerReward(long amount, int loanDuration, int level, Role role) {
        BigDecimal amountBigDecimal = new BigDecimal(amount);
        int daysOfYear = new DateTime().dayOfYear().getMaximumValue();

        double rewardRate = this.getRewardRate(level, Role.MERCHANDISER == role);

        return amountBigDecimal
                .multiply(new BigDecimal(rewardRate))
                .multiply(new BigDecimal(loanDuration))
                .divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN)
                .longValue();
    }

    private int calculateLoanDuration(LoanModel loanModel) {
        DateTime interestStartTime = new DateTime().withTimeAtStartOfDay();
        int loanDuration = 0;
        if (loanModel.getType().getLoanPeriodUnit() == LoanPeriodUnit.MONTH) {
            for (int index = 0; index < loanModel.getPeriods(); index++) {
                loanDuration += interestStartTime.plusDays(loanDuration).dayOfMonth().getMaximumValue();
            }
        }
        if (loanModel.getType().getLoanPeriodUnit() == LoanPeriodUnit.DAY) {
            loanDuration = loanModel.getPeriods();
        }
        return loanDuration;
    }

    private Role getReferrerPriorityRole(String referrerLoginName) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(referrerLoginName);

        if (CollectionUtils.isEmpty(userRoleModels)) {
            return null;
        }

        if (Iterators.tryFind(userRoleModels.iterator(), new Predicate<UserRoleModel>() {
            @Override
            public boolean apply(UserRoleModel input) {
                return input.getRole() == Role.MERCHANDISER;
            }
        }).isPresent()) {
            return Role.MERCHANDISER;
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

    private double getRewardRate(int level, boolean isMerchandiser) {
        try {
            if (isMerchandiser) {
                String[] split = this.referrerMerchandiserRoleReward.split("\\|");
                return level > split.length ? 0 : Double.parseDouble(split[level - 1]);
            } else {
                String[] split = this.referrerUserRoleReward.split("\\|");
                return level > split.length ? 0 : Double.parseDouble(split[level - 1]);
            }
        } catch (NumberFormatException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }
}
