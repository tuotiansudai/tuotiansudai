package coupon.service;

import com.tuotiansudai.enums.CouponType;
import coupon.dto.CouponAlertDto;

import java.util.List;

public interface CouponAlertService {

    CouponAlertDto getCouponAlert(String loginName, List<CouponType> couponTypes);

    void BirthdayNotify();

}
