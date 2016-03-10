package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponUserGroupModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUserGroupMapper {

    void create(CouponUserGroupModel couponUserGroupModel);

    void update(CouponUserGroupModel couponUserGroupModel);

    List<CouponUserGroupModel> findByCouponId(long couponId);


}
