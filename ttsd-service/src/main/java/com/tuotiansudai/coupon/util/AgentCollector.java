package com.tuotiansudai.coupon.util;

import com.tuotiansudai.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponUserGroupModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentCollector implements UserCollector {

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Override
    public List<String> collect(long couponId) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponId);
        return couponUserGroupModel != null ? couponUserGroupModel.getUserGroupItems() : null;
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (couponModel == null || userModel == null) {
            return false;
        }
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponModel.getId());
        return couponUserGroupModel != null && couponUserGroupModel.getUserGroupItems().contains(userModel.getLoginName());
    }
}
