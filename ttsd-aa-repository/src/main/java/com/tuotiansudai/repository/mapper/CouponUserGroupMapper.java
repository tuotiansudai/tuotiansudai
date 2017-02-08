package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CouponUserGroupModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUserGroupMapper {

    void create(CouponUserGroupModel couponUserGroupModel);

    void update(CouponUserGroupModel couponUserGroupModel);

    CouponUserGroupModel findByCouponId(long couponId);

    void delete(long id);

}
