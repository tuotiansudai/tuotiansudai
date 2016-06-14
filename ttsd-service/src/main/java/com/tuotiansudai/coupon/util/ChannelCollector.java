package com.tuotiansudai.coupon.util;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.coupon.repository.model.CouponUserGroupModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelCollector implements UserCollector{

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponId);
        if (couponUserGroupModel == null) {
            return null;
        }
        List<UserModel> userModels = userMapper.findUsersByChannel(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("channels", couponUserGroupModel.getUserGroupItems()).build()));
        return Lists.transform(userModels, new Function<UserModel, String>() {
            @Override
            public String apply(UserModel input) {
                return input.getLoginName();
            }
        });
    }

    @Override
    public boolean contains(long couponId, final String loginName) {
        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponId);
        if (couponUserGroupModel == null) {
            return false;
        }
        List<UserModel> userModels = userMapper.findUsersByChannel(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("channels", couponUserGroupModel.getUserGroupItems()).build()));
        return Iterators.any(userModels.iterator(), new Predicate<UserModel>() {
            @Override
            public boolean apply(UserModel input) {
                return input.getLoginName().equals(loginName);
            }
        });
    }

}
