package coupon.service;

import coupon.dto.UserCouponDto;
import coupon.repository.model.UserCouponView;

import java.util.List;

public interface UserCouponService {

    List<UserCouponDto> getInvestUserCoupons(String loginName, long loanId);

    List<UserCouponView> getUnusedUserCoupons(String loginName);

    List<UserCouponView> findUseRecords(String loginName);

    List<UserCouponView> getExpiredUserCoupons(String loginName);

    UserCouponDto getExperienceInvestUserCoupon(String loginName);

    boolean isUsableUserCouponExist(String loginName);

    long findSumBirthdayAndInterestByLoginName(String loginName);

    long findSumRedEnvelopeByLoginName(String loginName);
}
