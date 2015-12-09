package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CouponModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponMapper {

    void create(CouponModel couponModel);

    CouponModel findCouponById(long id);

}
