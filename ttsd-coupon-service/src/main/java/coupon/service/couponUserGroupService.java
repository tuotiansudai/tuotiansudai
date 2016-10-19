package coupon.service;

import coupon.repository.mapper.CouponUserGroupMapper;
import coupon.repository.model.CouponUserGroupModel;
import org.springframework.beans.factory.annotation.Autowired;

public class CouponUserGroupService {

    @Autowired
    CouponUserGroupMapper couponUserGroupMapper;

    public void create(CouponUserGroupModel couponUserGroupModel) {
        couponUserGroupMapper.create(couponUserGroupModel);
    }

    public void update(CouponUserGroupModel couponUserGroupModel) {
        couponUserGroupMapper.update(couponUserGroupModel);
    }

    public CouponUserGroupModel findByCouponId(long couponId) {
        return couponUserGroupMapper.findByCouponId(couponId);
    }

    public void delete(long id) {
        couponUserGroupMapper.delete(id);
    }
}
