package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.paywrapper.service.InvestAchievementService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InvestAchievementServiceImpl implements InvestAchievementService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    @Transactional
    public void awardAchievement(InvestModel investModel) {
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(investModel.getLoanId());

        if (isFirstInvestAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.FIRST_INVEST);
        }

        if (isMaxAmountAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.MAX_AMOUNT);
        }

        if (isLastInvestAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.LAST_INVEST);
        }

        investMapper.update(investModel);
    }

    private boolean isFirstInvestAchievement(InvestModel investModel, List<InvestModel> successInvestModels) {
        boolean isFirstInvestAchievementExist = Iterators.any(successInvestModels.iterator(), new Predicate<InvestModel>() {
            @Override
            public boolean apply(InvestModel input) {
                return input.getAchievements().contains(InvestAchievement.FIRST_INVEST);
            }
        });

        int firstInvestAchievementTimes = investMapper.countAchievementTimesByLoginName(investModel.getLoginName(),
                InvestAchievement.FIRST_INVEST,
                new DateTime().withTimeAtStartOfDay().dayOfMonth().withMinimumValue().toDate(),
                new Date());

        return !isFirstInvestAchievementExist && firstInvestAchievementTimes < 3;
    }

    private boolean isMaxAmountAchievement(InvestModel investModel, List<InvestModel> successInvestModels) {
        Ordering<InvestModel> ordering = new Ordering<InvestModel>() {
            @Override
            public int compare(InvestModel left, InvestModel right) {
                int compare = Long.compare(left.getAmount(), right.getAmount());
                if (compare == 0) {
                    return Longs.compare(right.getTradingTime().getTime(), left.getTradingTime().getTime());
                }
                return compare;
            }
        };

        InvestModel maxAmountInvestModel = ordering.max(successInvestModels);

        if (maxAmountInvestModel.getId() == investModel.getId()) {
            Optional<InvestModel> maxAmountInvestOptional = Iterators.tryFind(successInvestModels.iterator(), new Predicate<InvestModel>() {
                @Override
                public boolean apply(InvestModel input) {
                    return input.getAchievements().contains(InvestAchievement.MAX_AMOUNT);
                }
            });

            if (maxAmountInvestOptional.isPresent()) {
                InvestModel previousInvest = maxAmountInvestOptional.get();
                previousInvest.getAchievements().remove(InvestAchievement.MAX_AMOUNT);
                investMapper.update(previousInvest);
            }
            return true;
        }

        return false;
    }

    private boolean isLastInvestAchievement(InvestModel investModel, List<InvestModel> successInvestModels) {
        boolean isLastInvestAchievementExist = Iterators.any(successInvestModels.iterator(), new Predicate<InvestModel>() {
            @Override
            public boolean apply(InvestModel input) {
                return input.getAchievements().contains(InvestAchievement.LAST_INVEST);
            }
        });

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        long successInvestAmount = 0;
        for (InvestModel successInvestModel : successInvestModels) {
            successInvestAmount += successInvestModel.getAmount();
        }

        return !isLastInvestAchievementExist && loanModel.getLoanAmount() == successInvestAmount;
    }
}
