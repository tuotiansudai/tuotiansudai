package com.tuotiansudai.coupon.util;

import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.model.CouponType;

import java.util.List;

public interface InvestAchievementUserCollector {

    boolean contains(long couponId,long loanId, String loginName, UserGroup userGroup);
}
