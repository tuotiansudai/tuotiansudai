package com.tuotiansudai.coupon.util;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffCollector implements UserCollector{

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<String> collect(long couponId) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build()));
        return Lists.transform(userRoleModels, new Function<UserRoleModel, String>() {
            @Override
            public String apply(UserRoleModel input) {
                return input.getLoginName();
            }
        });
    }

    @Override
    public long count(long couponId) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build()));;
        return CollectionUtils.isNotEmpty(userRoleModels) ? userRoleModels.size() : 0;
    }

    @Override
    public boolean contains(long couponId, final String loginName) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", Lists.newArrayList()).build()));;
        return CollectionUtils.isNotEmpty(userRoleModels) && Iterators.any(userRoleModels.iterator(), new Predicate<UserRoleModel>() {
            @Override
            public boolean apply(UserRoleModel input) {
                return input.getLoginName().equals(loginName);
            }
        });
    }

}
