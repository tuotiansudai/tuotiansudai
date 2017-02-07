package com.tuotiansudai.coupon.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffCollector implements UserCollector{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        return userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build()));
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (userModel == null) {
            return false;
        }

        List<String> userRoleModels = userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build()));;
        return CollectionUtils.isNotEmpty(userRoleModels) && Iterators.any(userRoleModels.iterator(), input -> input.equalsIgnoreCase(userModel.getLoginName()));
    }

}
