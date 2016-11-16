package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.enums.UserBillBusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewbieCouponDiagnosis extends UserCouponDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(NewbieCouponDiagnosis.class);

    @Autowired
    public NewbieCouponDiagnosis(UserCouponMapper userCouponMapper, CouponRepayMapper couponRepayMapper) {
        super(userCouponMapper, couponRepayMapper);
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.NEWBIE_COUPON;
    }
}
