package coupon.service;

import coupon.dto.CouponDto;
import coupon.dto.ExchangeCouponDto;
import coupon.exception.CreateCouponException;
import coupon.repository.model.CouponModel;
import coupon.repository.model.UserCouponModel;

import java.util.Date;
import java.util.List;

public interface CouponService {

    ExchangeCouponDto createCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) throws CreateCouponException;

    void editCoupon(String loginName, ExchangeCouponDto exchangeCouponDto) throws CreateCouponException;

    List<CouponDto> findNewbieAndInvestCoupons(int index, int pageSize);

    int findNewbieAndInvestCouponsCount();

    CouponModel findCouponById(long couponId);

    List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime, int index, int pageSize);

    int findCouponDetailCount(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime);

    boolean deleteCoupon(String loginName, long couponId);

    List<CouponDto> findInterestCoupons(int index, int pageSize);

    int findInterestCouponsCount();

    List<CouponDto> findRedEnvelopeCoupons(int index, int pageSize);

    int findRedEnvelopeCouponsCount();

    List<CouponDto> findBirthdayCoupons(int index, int pageSize);

    int findBirthdayCouponsCount();
}
