package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.model.UserGroup;

public interface InvestAchievementUserCollector {

    boolean contains(long couponId, long loanId, String loginName, UserGroup userGroup);
}
