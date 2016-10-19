package coupon.service;

import coupon.repository.model.UserCouponModel;
import coupon.repository.model.UserGroup;

import java.util.List;

public interface CouponAssignmentService {

    boolean assignUserCoupon(String loginNameOrMobile, String exchangeCode);

    void assignUserCoupon(String loginNameOrMobile, long couponId);

    void assignUserCoupon(String loginNameOrMobile, List<UserGroup> userGroups);

    UserCouponModel assign(String loginName, long couponId, String exchangeCode);

    void assignUserCoupon(long loanId,String loginNameOrMobile, long couponId);
}
