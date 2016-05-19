package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.paywrapper.service.InvestAchievementService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class InvestAchievementServiceImpl implements InvestAchievementService {

    private static Logger logger = Logger.getLogger(InvestAchievementServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${invest.achievement.start.time}\")}")
    private Date achievementStartTime;

    @Override
    @Transactional
    public void awardAchievement(InvestModel investModel) {
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        if (loanModel.getFundraisingStartTime().before(achievementStartTime)) {
            return;
        }


        if (loanModel.getActivityType() == ActivityType.NEWBIE) {
            return;
        }

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(investModel.getLoanId());

        if (isFirstInvestAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.FIRST_INVEST);
            loanModel.setFirstInvestAchievementId(investModel.getId());
            logger.info(MessageFormat.format("{0} get the FIRST_INVEST of loan({1})", investModel.getLoginName(), String.valueOf(investModel.getLoanId())));
        }

        if (isMaxAmountAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.MAX_AMOUNT);
            loanModel.setMaxAmountAchievementId(investModel.getId());
            logger.info(MessageFormat.format("{0} get the MAX_AMOUNT of loan({1})", investModel.getLoginName(), String.valueOf(investModel.getLoanId())));
        }

        if (isLastInvestAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.LAST_INVEST);
            loanModel.setLastInvestAchievementId(investModel.getId());
            logger.info(MessageFormat.format("{0} get the LAST_INVEST of loan({1})", investModel.getLoginName(), String.valueOf(investModel.getLoanId())));
        }

        investMapper.update(investModel);
        loanMapper.update(loanModel);
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
        Optional<InvestModel> maxAmountAchievementInvestOptional = Iterators.tryFind(successInvestModels.iterator(), new Predicate<InvestModel>() {
            @Override
            public boolean apply(InvestModel input) {
                return input.getAchievements().contains(InvestAchievement.MAX_AMOUNT);
            }
        });

        if (maxAmountAchievementInvestOptional.isPresent()) {
            InvestModel previousMaxAmountInvest = maxAmountAchievementInvestOptional.get();

            long currentInvestorInvestAmount = 0;
            long maxAmountInvestorInvestAmount = 0;

            for (InvestModel successInvestModel : successInvestModels) {
                if (successInvestModel.getLoginName().equalsIgnoreCase(investModel.getLoginName())) {
                    currentInvestorInvestAmount += successInvestModel.getAmount();
                }
                if (successInvestModel.getLoginName().equalsIgnoreCase(previousMaxAmountInvest.getLoginName())) {
                    maxAmountInvestorInvestAmount += successInvestModel.getAmount();
                }
            }

            if (currentInvestorInvestAmount <= maxAmountInvestorInvestAmount) {
                return false;
            }

            previousMaxAmountInvest.getAchievements().remove(InvestAchievement.MAX_AMOUNT);
            investMapper.update(previousMaxAmountInvest);
        }

        return true;
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
