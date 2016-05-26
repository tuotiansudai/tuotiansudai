package com.tuotiansudai.coupon.util;


import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffRecommendLevelOneCollector implements UserCollector{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        return userMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build()));
    }

    @Override
    public boolean contains(long couponId, final String loginName) {
        List<String> referrerRelationModels = userMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build()));
        return CollectionUtils.isNotEmpty(referrerRelationModels) && Iterators.any(referrerRelationModels.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.equals(loginName);
            }
        });
    }

}
