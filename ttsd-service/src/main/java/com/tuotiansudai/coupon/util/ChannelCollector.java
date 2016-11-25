package com.tuotiansudai.coupon.util;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
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
        return Lists.transform(userModels, UserModel::getLoginName);
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        //该优惠券如果指定发给渠道用户,那么只发放给优惠券在活动期限内 且 来源渠道的用户注册时间也在活动期限内
        if (couponModel == null || userModel == null) {
            return false;
        }

        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponModel.getId());
        if (couponUserGroupModel == null) {
            return false;
        }

        List<UserModel> userModels = userMapper.findUsersByChannel(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("channels",
                couponUserGroupModel.getUserGroupItems()).build()));
        return Iterators.any(userModels.iterator(),
                input -> input.getLoginName().equalsIgnoreCase(userModel.getLoginName())
                        && (userModel.getRegisterTime().after(couponModel.getStartTime())
                        && userModel.getRegisterTime().before(couponModel.getEndTime())));
    }

}
