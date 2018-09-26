package com.tuotiansudai.coupon.util;

import com.google.common.collect.Iterators;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestAchievementCollector implements InvestAchievementUserCollector {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public boolean contains(final long couponId, final long loanId, String loginName, final UserGroup userGroup) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return false;
        }

        long investId = 0;
        if (UserGroup.FIRST_INVEST_ACHIEVEMENT.equals(userGroup)) {
            investId = loanModel.getFirstInvestAchievementId();
        } else if (UserGroup.MAX_AMOUNT_ACHIEVEMENT.equals(userGroup)) {
            investId = loanModel.getMaxAmountAchievementId();
        } else if (UserGroup.LAST_INVEST_ACHIEVEMENT.equals(userGroup)) {
            investId = loanModel.getLastInvestAchievementId();
        }

        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginNameAndCouponId(loginName, couponId);

        boolean isNotAssigned = CollectionUtils.isEmpty(userCouponModelList) || Iterators.all(userCouponModelList.iterator(), input -> input.getAchievementLoanId() != loanId);
        boolean isAchievementOwner = investMapper.findById(investId).getLoginName().equals(loginName);

        return isAchievementOwner && isNotAssigned;

    }
}
