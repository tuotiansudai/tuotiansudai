package coupon.util;

import coupon.repository.model.UserGroup;

public interface InvestAchievementUserCollector {

    boolean contains(long couponId,long loanId, String loginName, UserGroup userGroup);
}
