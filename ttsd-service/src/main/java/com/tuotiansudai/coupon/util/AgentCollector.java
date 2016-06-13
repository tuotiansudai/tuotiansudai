package com.tuotiansudai.coupon.util;

import com.tuotiansudai.coupon.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.coupon.repository.model.CouponUserGroupModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCollector implements UserCollector{

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Override
    public List<String> collect(long couponId) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponId);
        return couponUserGroupModel != null ? couponUserGroupModel.getUserGroupItems() : null;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponId);
        return couponUserGroupModel != null && couponUserGroupModel.getUserGroupItems().contains(loginName);
    }

}
