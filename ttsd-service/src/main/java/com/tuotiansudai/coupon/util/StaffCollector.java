package com.tuotiansudai.coupon.util;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Role;
import org.apache.commons.collections.CollectionUtils;
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
    public boolean contains(long couponId, final String loginName) {
        List<String> userRoleModels = userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build()));;
        return CollectionUtils.isNotEmpty(userRoleModels) && Iterators.any(userRoleModels.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.equals(loginName);
            }
        });
    }

}
