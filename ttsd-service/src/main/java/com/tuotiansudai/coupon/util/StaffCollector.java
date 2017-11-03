package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffCollector implements UserCollector {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        List<String> roles = Lists.newArrayList();
        roles.addAll(userMapper.findAllByRole(Role.ZC_STAFF));
        roles.addAll(userMapper.findAllByRole(Role.SD_STAFF));
        return roles;
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (userModel == null) {
            return false;
        }

        List<String> roles = Lists.newArrayList();
        roles.addAll(userMapper.findAllByRole(Role.ZC_STAFF));
        roles.addAll(userMapper.findAllByRole(Role.SD_STAFF));
        return CollectionUtils.isNotEmpty(roles) && roles.stream().anyMatch(l -> l.equalsIgnoreCase(userModel.getLoginName()));
    }

}
