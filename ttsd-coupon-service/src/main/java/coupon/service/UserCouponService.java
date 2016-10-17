package coupon.service;

import com.tuotiansudai.enums.CouponType;
import coupon.dto.UserCouponDto;
import coupon.repository.model.UserCouponModel;
import coupon.repository.model.UserCouponView;

import java.util.List;

public interface UserCouponService {

    UserCouponModel findById(long id);

    List<UserCouponDto> getInvestUserCoupons(String loginName, long loanId);

    List<UserCouponView> getUnusedUserCoupons(String loginName);

    List<UserCouponView> findUseRecords(String loginName);

    List<UserCouponView> getExpiredUserCoupons(String loginName);

    UserCouponDto getExperienceInvestUserCoupon(String loginName);

    boolean isUsableUserCouponExist(String loginName);

    long findSumBirthdayAndInterestByLoginName(String loginName);

    long findSumRedEnvelopeByLoginName(String loginName);

    List<UserCouponModel> findBirthdaySuccessByLoginNameAndInvestId(String loginName, long investId);

    List<UserCouponModel> findByInvestId(long investId);

    List<UserCouponModel> findByLoginName(String loginName, List<CouponType> couponTypes);

    List<UserCouponModel> findUserCouponSuccessAndCouponTypeByInvestId(long investId, List<CouponType> couponTypeList);

    List<UserCouponModel> findUserCouponSuccessByInvestId(long investId);

    void update(UserCouponModel userCouponModel);

    List<UserCouponModel> findByLoginNameAndCouponId(String loginName, Long couponId);
}
