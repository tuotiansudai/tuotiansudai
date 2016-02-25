package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.CouponExchangeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponExchangeMapper {

    void create(CouponExchangeModel couponExchangeModel);
}
