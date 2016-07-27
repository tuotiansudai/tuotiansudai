package com.tuotiansudai.coupon.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
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
    public boolean contains(final long couponId, long loanId, String loginName, UserGroup userGroup) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return false;
        }

        long investId = 0;
        if (UserGroup.FIRST_INVEST_ACHIEVEMENT.equals(userGroup)) {
            investId = loanModel.getFirstInvestAchievementId();
        }else if (UserGroup.MAX_AMOUNT_ACHIEVEMENT.equals(userGroup)) {
            investId = loanModel.getMaxAmountAchievementId();
        }else if (UserGroup.LAST_INVEST_ACHIEVEMENT.equals(userGroup)) {
            investId = loanModel.getLastInvestAchievementId();
        }

        if (investMapper.findById(investId).getLoginName().equals(loginName) && userCouponMapper.findCountByCouponIdAndUserGroup(couponId,userGroup,loginName) == 0) {
            return true;
        }

        return false;
    }
}
