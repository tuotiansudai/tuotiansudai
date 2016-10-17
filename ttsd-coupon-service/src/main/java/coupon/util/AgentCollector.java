package coupon.util;

import coupon.repository.model.CouponUserGroupModel;
import coupon.service.CouponUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCollector implements UserCollector {

    @Autowired
    private CouponUserGroupService couponUserGroupService;

    @Override
    public List<String> collect(long couponId) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupService.findByCouponId(couponId);
        return couponUserGroupModel != null ? couponUserGroupModel.getUserGroupItems() : null;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupService.findByCouponId(couponId);
        return couponUserGroupModel != null && couponUserGroupModel.getUserGroupItems().contains(loginName);
    }

}
