package com.tuotiansudai.coupon.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.commons.collections.CollectionUtils;
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
    public boolean contains(final long couponId,final long loanId, String loginName, final UserGroup userGroup) {
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
        if (CollectionUtils.isNotEmpty(userCouponModelList) && Iterators.any(userCouponModelList.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel input) {
                return input.getAchievementLoanId() == loanId;
            }
        })) {
            return false;
        }

        if (investMapper.findById(investId).getLoginName().equals(loginName)) {
            return true;
        }

        return false;
    }
}
