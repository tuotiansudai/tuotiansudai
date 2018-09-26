package com.tuotiansudai.coupon.util;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserRecommendationMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffRecommendLevelOneCollector implements UserCollector {

    @Autowired
    private UserRecommendationMapper userMapper;

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (userModel == null) {
            return false;
        }

        List<String> referrerRelationModels = userMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList()).build()));
        return CollectionUtils.isNotEmpty(referrerRelationModels) && Iterators.any(referrerRelationModels.iterator(), input -> input.equalsIgnoreCase(userModel.getLoginName()));
    }

}
