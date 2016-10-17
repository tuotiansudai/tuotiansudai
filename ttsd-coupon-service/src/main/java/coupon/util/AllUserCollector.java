package coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.util.UserBirthdayUtil;
import coupon.repository.model.CouponModel;
import coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllUserCollector implements UserCollector {

    @Autowired
    private UserBirthdayUtil userBirthdayUtil;

    @Autowired
    private CouponService couponService;

    @Override
    public List<String> collect(long couponId) {
        return Lists.newArrayList();
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponModel couponModel = couponService.findById(couponId);
        return couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON || userBirthdayUtil.isBirthMonth(loginName);
    }
}
