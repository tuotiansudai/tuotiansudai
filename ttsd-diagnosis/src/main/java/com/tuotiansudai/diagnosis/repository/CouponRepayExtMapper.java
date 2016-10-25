package com.tuotiansudai.diagnosis.repository;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface CouponRepayExtMapper {

    List<CouponRepayModel> findCouponRepayByUserCouponIdAndDate(long userCouponId, Date beginDate, Date endDate);
}
